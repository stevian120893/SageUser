package com.mib.feature_home.contents.category_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.CategoryHorizontalAdapter
import com.mib.feature_home.adapter.SubcategoriesPagingAdapter
import com.mib.feature_home.contents.category_list.CategoryListViewModel.Companion.EVENT_UPDATE_CATEGORY
import com.mib.feature_home.contents.category_list.CategoryListViewModel.Companion.EVENT_SELECT_CATEGORY
import com.mib.feature_home.contents.category_list.CategoryListViewModel.Companion.EVENT_UPDATE_SUBCATEGORY
import com.mib.feature_home.databinding.FragmentCategoryListBinding
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListFragment : BaseFragment<CategoryListViewModel>(0) {

    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!
    private var categoryHorizontalAdapter: CategoryHorizontalAdapter? = null
    private var subcategoriesPagingAdapter: SubcategoriesPagingAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToHomeScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(CategoryListViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@CategoryListFragment, backPressedCallback)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCategories(this@CategoryListFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.HORIZONTAL, binding.rvCategory)
            AppUtils.firstSetRecyclerViewGrid(view.context, binding.rvSubcategory, 2)
            initListener()
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        categoryHorizontalAdapter = null
    }

    private fun initListener() {
        binding.rvSubcategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val subcategoriesSize = subcategoriesPagingAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && subcategoriesSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchSubcategories(this@CategoryListFragment, nextCursor.orEmpty())
                            subcategoriesPagingAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.llSearch.setOnClickListener {
            viewModel.goToProductListScreen(
                navController = findNavController(),
                isSearch = true
            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }

        binding.srlSubcategory.setOnRefreshListener {
            viewModel.fetchSubcategories(this@CategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }

        binding.llNoData.setOnClickListener {
            viewModel.fetchSubcategories(this@CategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state.event) {
                EVENT_UPDATE_CATEGORY -> {
                    if (binding.srlSubcategory.isRefreshing) binding.srlSubcategory.isRefreshing = false

                    if(state.isLoadCategories) {
                        binding.rvCategory.visibility = View.GONE
                        binding.sflCategory.visibility = View.VISIBLE
                        binding.sflSubcategory.visibility = View.VISIBLE
                    } else {
                        binding.sflCategory.visibility = View.GONE
                        binding.sflSubcategory.visibility = View.GONE
                        binding.rvCategory.visibility = View.VISIBLE
                        state.categoriesItemPaging?.let { categoriesItem ->
                            if(categoriesItem.items?.isNotEmpty() == true) {
                                binding.rvCategory.visibility = View.VISIBLE
                                setupCategoryAdapter(categoriesItem.items)
                                updateCategoryAndFetchSubcategory(state.categoriesItemPaging?.items?.get(0)?.categoryCode.orEmpty())
                            } else {
                                binding.rvCategory.visibility = View.GONE
                            }
                        }
                    }
                }
                EVENT_UPDATE_SUBCATEGORY -> {
                    state.isLoadSubcategories?.let {
                        if (binding.srlSubcategory.isRefreshing) binding.srlSubcategory.isRefreshing = false

                        if(it) {
                            binding.srlSubcategory.visibility = View.GONE
                            binding.sflSubcategory.visibility = View.VISIBLE
                            binding.llNoData.visibility = View.GONE
                        } else {
                            binding.sflSubcategory.visibility = View.GONE
                            state.subcategoriesItemPaging?.let { subcategoriesItem ->
                                if(subcategoriesItem.items?.isNotEmpty() == true) {
                                    binding.srlSubcategory.visibility = View.VISIBLE
                                    binding.llNoData.visibility = View.GONE
                                    nextCursor = subcategoriesItem.nextCursor
                                    val hasMoreItem = subcategoriesItem.nextCursor != null
                                    if(hasMoreItem) {
                                        val cursor = nextCursor?.toInt() ?: -1
                                        if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                            subcategoriesPagingAdapter?.removeLoadingFooter()
                                            subcategoriesPagingAdapter?.addList(subcategoriesItem.items.toMutableList())
                                            isLoadNextItem = false
                                        } else { // first fetch
                                            setupSubcategoryAdapter(subcategoriesItem.items)
                                        }
                                    } else {
                                        if(isLoadNextItem) {
                                            subcategoriesPagingAdapter?.removeLoadingFooter()
                                            isLoadNextItem = false
                                        } else {
                                            setupSubcategoryAdapter(subcategoriesItem.items)
                                        }
                                    }
                                } else {
                                    binding.srlSubcategory.visibility = View.GONE
                                    binding.llNoData.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
                EVENT_SELECT_CATEGORY -> onSelectedProductItem(state)
            }
        }
    }

    private fun updateCategoryAndFetchSubcategory(categoryCode: String) {
        val hasNoSelectedItem = viewModel.categoryCode.isNullOrEmpty()

        val pos = if(hasNoSelectedItem) {
            viewModel.updateCategoryCode(categoryCode)
            0
        } else categoryHorizontalAdapter?.getItemIndex(viewModel.categoryCode) ?: 0
        categoryHorizontalAdapter?.setItemSelected(pos, true)
        viewModel.updateSelectedIndex(pos)
        binding.rvCategory.scrollToPosition(pos)

        viewModel.fetchSubcategories(this@CategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    private fun onSelectedProductItem(state: CategoryListViewModel.ViewState) {
        state.lastSelectedItemIndex?.let { categoryHorizontalAdapter?.setItemSelected(it, false) }
        state.selectedItemIndex?.let { categoryHorizontalAdapter?.setItemSelected(it, true) }
        viewModel.updateCategoryCode(state.selectedItem?.categoryCode)
        viewModel.fetchSubcategories(this@CategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    private fun setupCategoryAdapter(categories: List<Category>) {
        categoryHorizontalAdapter = CategoryHorizontalAdapter(
            itemList = categories.toMutableList(),
            onItemClickListener = object : CategoryHorizontalAdapter.OnItemClickListener {
                override fun onClick(category: Category, pos: Int) {
                   viewModel.onClickProductItem(pos, category)
                }
            }
        )
        binding.rvCategory.adapter = categoryHorizontalAdapter
    }

    private fun setupSubcategoryAdapter(subcategories: List<Subcategory>) {
        subcategoriesPagingAdapter = SubcategoriesPagingAdapter(
            itemList = subcategories.toMutableList(),
            onItemClickListener = object : SubcategoriesPagingAdapter.OnItemClickListener {
                override fun onClick(subcategory: Subcategory) {
                    viewModel.goToProductListScreen(
                        findNavController(),
                        subcategory.categoryCode,
                        subcategory.subcategoryId,
                        subcategory.subcategoryName
                    )
                }
            }
        )
        binding.rvSubcategory.adapter = subcategoriesPagingAdapter
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2

        const val KEY_CATEGORY_CODE = "key_category_code"
    }
}