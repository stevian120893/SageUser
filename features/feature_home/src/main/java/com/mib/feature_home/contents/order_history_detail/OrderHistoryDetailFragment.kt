package com.mib.feature_home.contents.order_history_detail

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailViewModel.Companion.EVENT_UPDATE_ORDER_DETAIL
import com.mib.feature_home.databinding.FragmentOrderHistoryDetailBinding
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.PENDING_PAYMENT_APPROVAL
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.WAITING_FOR_PAYMENT
import com.mib.feature_home.utils.CustomUtils
import com.mib.feature_home.utils.createEasyImage
import com.mib.feature_home.utils.withThousandSeparator
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

@AndroidEntryPoint
class OrderHistoryDetailFragment : BaseFragment<OrderHistoryDetailViewModel>(0) {

    private var _binding: FragmentOrderHistoryDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var paymentReceiptImage: MultipartBody.Part? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.onBackPressed(this@OrderHistoryDetailFragment)
            findNavController().popBackStack()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(OrderHistoryDetailViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@OrderHistoryDetailFragment, backPressedCallback)
        viewModel.getOrderDetail(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData(view.context)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getOrderDetail(findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        binding.ivBack.setOnClickListener {
            viewModel.onBackPressed(this@OrderHistoryDetailFragment)
        }

        binding.btPay.setOnClickListener {
            viewModel.payOrder(context, paymentReceiptImage)
        }

        binding.ivAddPhotoReceipt.setOnClickListener {
            viewModel.showUploadOptionDialog(this@OrderHistoryDetailFragment, easyImage)
        }

        binding.btGiveRating.setOnClickListener {
            viewModel.showRatingDialog(this@OrderHistoryDetailFragment)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when(state.event) {
                EVENT_UPDATE_ORDER_DETAIL -> {
                    if(state.isLoadOrderDetail) {
                        binding.llContent.visibility = View.GONE
                        binding.sflProductDetail.visibility = View.VISIBLE
                    } else {
                        binding.llContent.visibility = View.VISIBLE
                        binding.sflProductDetail.visibility = View.GONE

//                        Glide.with(this@ProductDetailFragment).load(state.orderDetail?.p).into(binding.ivProduct)
                        binding.tvOrderName.text = state.orderDetail?.detail?.product?.name.orEmpty()
                        binding.tvMerchantName.text = state.orderDetail?.merchant?.name.orEmpty()
                        binding.tvCityName.text = state.orderDetail?.address.orEmpty()
                        binding.tvStatus.text = CustomUtils.getUserFriendlyOrderStatusName(context, state.orderDetail?.status.orEmpty())

                        binding.tvDate.text = state.orderDetail?.bookingDate
                        binding.tvPrice.text = context.getString(R.string.currency_format, state.orderDetail?.totalPrice.toString().withThousandSeparator())
                        binding.tvPaymentType.text = state.orderDetail?.usedPaymentMethod.toString()

                        binding.tvNotes.text = state.orderDetail?.note.toString()
                        binding.tvAddress.text = state.orderDetail?.address.toString()

                        when(state.orderDetail?.status) {
                            OrderDetail.NEGOTIATING -> {
                                binding.llAction.visibility = View.GONE
                                binding.btPay.visibility = View.GONE
                                binding.btGiveRating.visibility = View.GONE
                            }
                            WAITING_FOR_PAYMENT -> {
                                binding.llAction.visibility = View.VISIBLE
                                if(state.orderDetail?.usedPaymentMethod == KEY_PAYMENT_METHOD_TRANSFER) {
                                    binding.llPaymentReceipt.visibility = View.VISIBLE
                                }
                                binding.btPay.visibility = View.VISIBLE
                                binding.btGiveRating.visibility = View.GONE
                            }
                            PENDING_PAYMENT_APPROVAL -> {}
                            OrderDetail.ONGOING -> {
                                binding.llAction.visibility = View.GONE
                                binding.btPay.visibility = View.GONE
                                binding.btGiveRating.visibility = View.GONE
                            }
                            OrderDetail.CANCEL -> {
                                // nothing
                            }
                            OrderDetail.DONE -> {
                                binding.llAction.visibility = View.VISIBLE
                                binding.btPay.visibility = View.GONE
                                binding.btGiveRating.visibility = View.VISIBLE
                            }
                            else -> {
                                binding.llAction.visibility = View.GONE
                                binding.btPay.visibility = View.GONE
                                binding.btGiveRating.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            34964, 34962 -> {
                easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), object: DefaultCallback() {
                    override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                        lifecycleScope.launchWhenResumed {
                            val compressedFile = Compressor.compress(requireActivity(), imageFiles[0].file) {
                                quality(50)
                            }
                            val myBitmap = BitmapFactory.decodeFile(imageFiles[0].file.absolutePath)

                            binding.ivAddPhotoReceipt.setImageBitmap(myBitmap)
                            paymentReceiptImage = MultipartBody.Part.createFormData(
                                "payment_receipt_image",
                                compressedFile.name,
                                compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            )
                        }
                    }
                })
            }
        }
    }

    companion object {
        const val KEY_ORDER_ID = "KEY_ORDER_ID"
        const val KEY_PAYMENT_METHOD_DANA = "DANA"
        const val KEY_PAYMENT_METHOD_TRANSFER = "TRANSFER"
        const val KEY_PAYMENT_METHOD_CASH = "CASH"
        const val KEY_IS_FROM_CHECKOUT = "KEY_IS_FROM_CHECKOUT"
    }
}