package com.mib.feature_home.contents.product_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.ProductsAdapter
import com.mib.feature_home.contents.product_list.ProductListViewModel.Companion.EVENT_UPDATE_PRODUCT_LIST
import com.mib.feature_home.databinding.FragmentProductListBinding
import com.mib.feature_home.domain.model.Product
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : BaseFragment<ProductListViewModel>(0) {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private var productsAdapter: ProductsAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProductListViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@ProductListFragment, backPressedCallback)
        viewModel.fetchProducts(this@ProductListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
//            setupAdapter(requireContext())
            AppUtils.firstSetRecyclerViewGrid(view.context, binding.rvProduct, 2)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productsAdapter = null
        nextCursor = null
    }

    private fun initListener() {
        binding.rvProduct.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val productsSize = productsAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && productsSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchProducts(this@ProductListFragment, nextCursor.orEmpty())
                            productsAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.srlProduct.setOnRefreshListener {
            viewModel.fetchProducts(this@ProductListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            if(state.isLoadProducts) {
                binding.llContent.visibility = View.GONE
                binding.sflProduct.visibility = View.VISIBLE
            } else {
                if (binding.srlProduct.isRefreshing) binding.srlProduct.isRefreshing = false
            }

            when (state.event) {
                EVENT_UPDATE_PRODUCT_LIST -> {
                    binding.sflProduct.visibility = View.GONE
                    binding.llContent.visibility = View.VISIBLE
                    state.productsItemPaging?.let { productsItem ->
                        if(productsItem.items?.isNotEmpty() == true) {
                            binding.rvProduct.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                            nextCursor = productsItem.nextCursor
                            val hasMoreItem = productsItem.nextCursor != null
                            if(hasMoreItem) {
                                val cursor = nextCursor?.toInt() ?: -1
                                if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                    productsAdapter?.removeLoadingFooter()
                                    productsAdapter?.addList(productsItem.items?.toMutableList())
                                    isLoadNextItem = false
                                } else { // first fetch
                                    productsItem.items?.let { setupAdapter(context, it) }
                                }
                            } else {
                                if(isLoadNextItem) {
                                    productsAdapter?.removeLoadingFooter()
                                    isLoadNextItem = false
                                } else {
                                    productsItem.items?.let { setupAdapter(context, it) }
                                }
                            }
                        } else {
                            binding.rvProduct.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewModel.subcategoryNameLiveData.observe(viewLifecycleOwner) {
            binding.tvSubcategoryName.text = it
        }
    }

    private fun setupAdapter(context: Context, products: List<Product>) {
        productsAdapter = ProductsAdapter(
            context = context,
            itemList = products.toMutableList(),
            onItemClickListener = object : ProductsAdapter.OnItemClickListener {
                override fun onClick(product: Product) {
                    if(viewModel.isLoggedIn(findNavController()))
                        viewModel.goToProductDetail(findNavController(), product.productCode)
                }
            }
        )
        binding.rvProduct.adapter = productsAdapter
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2

        const val KEY_CATEGORY_CODE = "key_category_code"
        const val KEY_SUBCATEGORY_CODE = "key_subcategory_code"
        const val KEY_SUBCATEGORY_NAME = "key_subcategory_name"
    }
}