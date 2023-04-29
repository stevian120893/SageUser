package com.mib.feature_home.contents.bottom_menu.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.adapter.BannerAdapter
import com.mib.feature_home.adapter.CategoriesAdapter
import com.mib.feature_home.contents.bottom_menu.home.HomeViewModel.Companion.EVENT_UPDATE_FIRST_ITEMS
import com.mib.feature_home.contents.bottom_menu.home.HomeViewModel.Companion.EVENT_UPDATE_LOCATION
import com.mib.feature_home.contents.category_list.CategoryListFragment
import com.mib.feature_home.databinding.FragmentHomeBinding
import com.mib.feature_home.domain.model.Banner
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>(0) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@HomeFragment, backPressedCallback)
        viewModel.initGps(requireContext())
        viewModel.getHomeContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)

        lifecycleScope.launch {
            AppUtils.firstSetRecyclerViewGrid(view.context, binding.rvCategory, 2)
            initListener(view.context)
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        binding.textSeeAll.setOnClickListener {
            viewModel.goToCategoryListScreen(findNavController())
        }

        binding.tvLocation.setOnClickListener {
            viewModel.chooseLocation(context)
        }

        binding.srlHome.setOnRefreshListener {
            viewModel.getHomeContent()
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            if (binding.srlHome.isRefreshing) binding.srlHome.isRefreshing = false

            when(it.event) {
                EVENT_UPDATE_FIRST_ITEMS -> {
                    if(it.isLoadHome) {
                        binding.llBanner.visibility = View.GONE
                        binding.rvCategory.visibility = View.GONE
                        binding.sflBanner.visibility = View.VISIBLE
                        binding.sflCategoryList.visibility = View.VISIBLE
                    } else {
                        binding.llBanner.visibility = View.VISIBLE
                        binding.rvCategory.visibility = View.VISIBLE
                        binding.sflBanner.visibility = View.GONE
                        binding.sflCategoryList.visibility = View.GONE
                        val bannerAdapter = BannerAdapter(it.banner.orEmpty(),
                            object : BannerAdapter.OnItemClickListener {
                                override fun onClick(banner: Banner) {

                                }
                            })
                        binding.vpBanner.adapter = bannerAdapter
                        setupCategoryAdapter(it.category.orEmpty())
                    }
                }
                EVENT_UPDATE_LOCATION -> {
                    if(it.location != null) {
                        binding.tvLocation.text = it.location?.second.orEmpty()
                    }
                }
            }
        }
    }

    private fun setupCategoryAdapter(categories: List<Category>) {
        binding.rvCategory.adapter = CategoriesAdapter(
            itemList = categories.toMutableList(),
            onItemClickListener = object : CategoriesAdapter.OnItemClickListener {
                override fun onClick(category: Category) {
                    if(category.categoryCode == CATEGORY_ALL) {
                        viewModel.goToCategoryListScreen(findNavController())
                    } else {
                        viewModel.goToSubcategoryListScreen(
                            findNavController(),
                            category.categoryCode,
                            category.categoryName
                        )
                    }
                }
            }
        )
        binding.diBanner.attachTo(binding.vpBanner)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            GPS_REQUEST_CODE -> {
                viewModel.actionWhenGpsTurnedOn(requireContext())
            }
        }
    }

    companion object {
        const val GPS_REQUEST_CODE = 1001
        const val CATEGORY_ALL = "ALL"
    }
}