package com.mib.feature_home.contents.order_history_detail

import android.content.Context
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
import com.mib.feature_home.utils.dateToString
import com.mib.feature_home.utils.stringToDate
import com.mib.feature_home.utils.withThousandSeparator
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderHistoryDetailFragment : BaseFragment<OrderHistoryDetailViewModel>(0) {

    private var _binding: FragmentOrderHistoryDetailBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
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
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
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

//                        Glide.with(this@ProductDetailFragment).load(state.productDetail?.imageUrl).into(binding.ivProduct)
                        binding.tvOrderName.text = state.orderDetail?.detail?.product?.name.orEmpty()
                        binding.tvMerchantName.text = state.orderDetail?.merchant?.name.orEmpty()
                        binding.tvCityName.text = state.orderDetail?.address.orEmpty()

                        binding.tvDate.text = state.orderDetail?.bookingDate
                        binding.tvPrice.text = context.getString(R.string.currency_format, state.orderDetail?.totalPrice.toString().withThousandSeparator())
                        binding.tvPaymentType.text = state.orderDetail?.usedPaymentMethod.toString()

                        binding.tvNotes.text = state.orderDetail?.note.toString()
                        binding.tvAddress.text = state.orderDetail?.address.toString()
                    }
                }
//                ProductDetailViewModel.EVENT_ORDER_SUCCEED -> {
//                    AppUtils.openWhatsApp(context, context.getString(R.string.shared_res_whatsapp_number))
//                }
            }
        }
    }

    companion object {
        const val KEY_ORDER_ID = "KEY_ORDER_ID"
    }
}