package com.mib.feature_home.contents.bottom_menu.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.adapter.BannerAdapter
import com.mib.feature_home.adapter.CategoriesAdapter
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
            observeLiveData(view.context)
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
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            val bannerAdapter = BannerAdapter(it.banner.orEmpty(), object : BannerAdapter.OnItemClickListener {
                override fun onClick(banner: Banner) {

                }
            })
            binding.vpBanner.adapter = bannerAdapter
            setupCategoryAdapter(context, it.category.orEmpty())
        }
    }

    private fun setupCategoryAdapter(context: Context, categories: List<Category>) {
        binding.rvCategory.adapter = CategoriesAdapter(
            context = context,
            itemList = categories.toMutableList(),
            onItemClickListener = object : CategoriesAdapter.OnItemClickListener {
                override fun onClick(category: Category) {
                    viewModel.goToSubcategoryListScreen(findNavController(), category.categoryId, category.categoryName)
                }
            }
        )
        binding.diBanner.attachTo(binding.vpBanner)
    }
}