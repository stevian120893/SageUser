package com.mib.feature_home.contents.subcategory_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.SubcategoriesPagingAdapter
import com.mib.feature_home.contents.subcategory_list.SubcategoryListViewModel.Companion.EVENT_UPDATE_LIST
import com.mib.feature_home.databinding.FragmentSubcategoryListBinding
import com.mib.feature_home.domain.model.Subcategory
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubcategoryListFragment : BaseFragment<SubcategoryListViewModel>(0) {

    private var _binding: FragmentSubcategoryListBinding? = null
    private val binding get() = _binding!!
    private var subcategoriesPagingAdapter: SubcategoriesPagingAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.goToHomeScreen(findNavController())
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(SubcategoryListViewModel::class.java)
        viewModel.init(arguments)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@SubcategoryListFragment, backPressedCallback)
        viewModel.fetchSubcategories(this@SubcategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubcategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)
        lifecycleScope.launch {
            AppUtils.firstSetRecyclerViewGrid(view.context, binding.rvSubcategory, 4)
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        subcategoriesPagingAdapter = null
        nextCursor = null
    }

    private fun initListener() {
        binding.rvSubcategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val subcategoriesSize = subcategoriesPagingAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && subcategoriesSize >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchSubcategories(this@SubcategoryListFragment, nextCursor.orEmpty())
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

        binding.llNoData.setOnClickListener {
            viewModel.fetchSubcategories(this@SubcategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToHomeScreen(findNavController())
        }

        binding.srlSubcategory.setOnRefreshListener {
            viewModel.fetchSubcategories(this@SubcategoryListFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            state.isLoadSubcategories?.let {
                if(it) {
                    binding.llContent.visibility = View.GONE
                    binding.sflSubcategory.visibility = View.VISIBLE
                } else {
                    if (binding.srlSubcategory.isRefreshing) binding.srlSubcategory.isRefreshing = false
                }
            }

            when (state.event) {
                EVENT_UPDATE_LIST -> {
                    binding.sflSubcategory.visibility = View.GONE
                    binding.llContent.visibility = View.VISIBLE
                    state.subcategoriesItemPaging?.let { subcategoriesItem ->
                        if(subcategoriesItem.items?.isNotEmpty() == true) {
                            binding.rvSubcategory.visibility = View.VISIBLE
                            binding.llNoData.visibility = View.GONE
                            nextCursor = subcategoriesItem.nextCursor
                            val hasMoreItem = subcategoriesItem.nextCursor != null
                            if(hasMoreItem) {
                                val cursor = nextCursor?.toInt() ?: -1
                                if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                    subcategoriesPagingAdapter?.removeLoadingFooter()
                                    subcategoriesPagingAdapter?.addList(subcategoriesItem.items?.toMutableList())
                                    isLoadNextItem = false
                                } else { // first fetch
                                    subcategoriesItem.items?.let { setupAdapter(context, it) }
                                }
                            } else {
                                if(isLoadNextItem) {
                                    subcategoriesPagingAdapter?.removeLoadingFooter()
                                    isLoadNextItem = false
                                } else {
                                    subcategoriesItem.items?.let { setupAdapter(context, it) }
                                }
                            }
                        } else {
                            binding.rvSubcategory.visibility = View.GONE
                            binding.llNoData.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewModel.categoryCodeLiveData.observe(viewLifecycleOwner) {
            binding.tvCategoryName.text = it
        }
    }

    private fun setupAdapter(context: Context, subcategories: List<Subcategory>) {
        subcategoriesPagingAdapter = SubcategoriesPagingAdapter(
            context = context,
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
        const val KEY_CATEGORY_NAME = "key_category_name"
    }
}