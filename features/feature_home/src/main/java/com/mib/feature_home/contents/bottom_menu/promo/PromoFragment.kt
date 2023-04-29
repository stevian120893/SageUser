package com.mib.feature_home.contents.bottom_menu.promo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.PromoAdapter
import com.mib.feature_home.databinding.FragmentPromoListBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromoFragment : BaseFragment<PromoViewModel>(0) {

    private var _binding: FragmentPromoListBinding? = null
    private val binding get() = _binding!!

    private var productsAdapter: PromoAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(PromoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@PromoFragment, backPressedCallback)
//        viewModel.fetchProducts(this@ProductListFragment, ProductListFragment.DEFAULT_NEXT_CURSOR_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)

        lifecycleScope.launch {
            initListener()
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.rvPromo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                val productsSize = productsAdapter?.itemList?.size ?: -1
//                if (!isLoadNextItem && productsSize >= ProductListFragment.MAX_PAGINATION_ITEMS) {
//                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
//                        nextCursor?.let {
//                            viewModel.fetchProducts(this@ProductListFragment, nextCursor.orEmpty())
//                            productsAdapter?.addLoadingFooter()
//                            isLoadNextItem = true
//                        }
//                    }
//                }
            }
        })

        binding.srlPromo.setOnRefreshListener {
//            viewModel.fetchProducts(this@ProductListFragment, ProductListFragment.DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
//            if(state.isLoadProducts) {
//                binding.llContent.visibility = View.GONE
//                binding.sflPromo.visibility = View.VISIBLE
//            } else {
//                if (binding.srlPromo.isRefreshing) binding.srlPromo.isRefreshing = false
//            }
//
//            when (state.event) {
//                ProductListViewModel.EVENT_UPDATE_PRODUCT_LIST -> {
//                    binding.sflProduct.visibility = View.GONE
//                    binding.llContent.visibility = View.VISIBLE
//                    state.productsItemPaging?.let { productsItem ->
//                        if(productsItem.items?.isNotEmpty() == true) {
//                            binding.rvProduct.visibility = View.VISIBLE
//                            binding.tvNoData.visibility = View.GONE
//                            nextCursor = productsItem.nextCursor
//                            val hasMoreItem = productsItem.nextCursor != null
//                            if(hasMoreItem) {
//                                val cursor = nextCursor?.toInt() ?: -1
//                                if (cursor > ProductListFragment.DEFAULT_NEXT_CURSOR_RESPONSE) {
//                                    productsAdapter?.removeLoadingFooter()
//                                    productsAdapter?.addList(productsItem.items?.toMutableList())
//                                    isLoadNextItem = false
//                                } else { // first fetch
//                                    productsItem.items?.let { setupAdapter(context, it) }
//                                }
//                            } else {
//                                if(isLoadNextItem) {
//                                    productsAdapter?.removeLoadingFooter()
//                                    isLoadNextItem = false
//                                } else {
//                                    productsItem.items?.let { setupAdapter(context, it) }
//                                }
//                            }
//                        } else {
//                            binding.rvProduct.visibility = View.GONE
//                            binding.tvNoData.visibility = View.VISIBLE
//                        }
//                    }
//                }
//            }
        }
    }

//    private fun setupAdapter(context: Context, products: List<Promo>) {
//        productsAdapter = PromoAdapter(
//            context = context,
//            itemList = products.toMutableList(),
//            onItemClickListener = object : PromoAdapter.OnItemClickListener {
//                override fun onClick(product: Product) {
//                    if(viewModel.isLoggedIn(findNavController()))
//                        viewModel.goToProductDetail(findNavController(), product.productCode)
//                }
//            }
//        )
//        binding.rvPromo.adapter = productsAdapter
//    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}