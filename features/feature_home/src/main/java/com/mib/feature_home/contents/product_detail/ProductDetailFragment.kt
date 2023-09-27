package com.mib.feature_home.contents.product_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.contents.product_detail.ProductDetailViewModel.Companion.EVENT_ORDER_SUCCEED
import com.mib.feature_home.contents.product_detail.ProductDetailViewModel.Companion.EVENT_UPDATE_PRODUCT
import com.mib.feature_home.databinding.FragmentProductDetailBinding
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.withThousandSeparator
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<ProductDetailViewModel>(0) {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProductDetailViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@ProductDetailFragment, backPressedCallback)
        viewModel.getProductDetail(findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
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
        binding.tvContactUs.setOnClickListener {
            AppUtils.openWhatsApp(context, context.getString(R.string.shared_res_whatsapp_number))
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btOrder.setOnClickListener {
            viewModel.confirmOrder(context)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when(state.event) {
                EVENT_UPDATE_PRODUCT -> {
                    if(state.isLoadProduct) {
                        binding.rlContent.visibility = View.GONE
                        binding.sflProductDetail.visibility = View.VISIBLE
                    } else {
                        binding.rlContent.visibility = View.VISIBLE
                        binding.sflProductDetail.visibility = View.GONE

                        Glide.with(this@ProductDetailFragment).load(state.productDetail?.imageUrl).into(binding.ivProduct)
                        binding.tvProductName.text = state.productDetail?.name.orEmpty()
                        binding.tvPrice.text = context.getString(R.string.currency_format, state.productDetail?.price.orEmpty().withThousandSeparator())
                        binding.tvTime.text = state.productDetail?.operationTime.orEmpty()
                        binding.tvLocation.text = state.productDetail?.location?.name.orEmpty()
                        binding.tvServiceUsed.text = getString(R.string.service_used, state.productDetail?.transactionUsage.toString())
                        binding.tvYearsService.text = getString(R.string.years, state.productDetail?.serviceYearsExperience.toString())
                        binding.tvMerchantName.text = state.productDetail?.merchantName.toString()
                        binding.tvDescription.text = state.productDetail?.description.toString()
                        binding.tvRating.text = getString(R.string.rating, state.productDetail?.rating.toString())

                        // TODO payment acceptance
                    }
                }
                EVENT_ORDER_SUCCEED -> {
                    AppUtils.openWhatsApp(context, context.getString(R.string.shared_res_whatsapp_number))
                }
            }
        }
    }

    companion object {
        const val KEY_PRODUCT_CODE = "key_product_code"
    }
}