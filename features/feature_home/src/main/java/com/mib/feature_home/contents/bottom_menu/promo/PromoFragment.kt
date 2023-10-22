package com.mib.feature_home.contents.bottom_menu.promo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.PromoAdapter
import com.mib.feature_home.databinding.FragmentPromoListBinding
import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromoFragment : BaseFragment<PromoViewModel>(0) {

    private var _binding: FragmentPromoListBinding? = null
    private val binding get() = _binding!!

    private var promoAdapter: PromoAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(PromoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@PromoFragment, backPressedCallback)
        if(viewModel.isLoggedIn()) viewModel.fetchPromo(this@PromoFragment, DEFAULT_NEXT_CURSOR_REQUEST)
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
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvPromo)
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
                val size = promoAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && size >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.fetchPromo(this@PromoFragment, nextCursor.orEmpty())
                            promoAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.srlPromo.setOnRefreshListener {
            viewModel.fetchPromo(this@PromoFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }

        binding.llNoData.setOnClickListener {
            viewModel.fetchPromo(this@PromoFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            if(state.isLoadPromo) {
                if(state.shouldShowShimmer) {
                    binding.llContent.visibility = View.GONE
                    binding.sflPromo.visibility = View.VISIBLE
                }
            } else {
                if (binding.srlPromo.isRefreshing) binding.srlPromo.isRefreshing = false
            }

            binding.sflPromo.visibility = View.GONE
            binding.llContent.visibility = View.VISIBLE
            state.promoItemPaging?.let { paging ->
                if(paging.items?.isNotEmpty() == true) {
                    binding.rvPromo.visibility = View.VISIBLE
                    binding.llNoData.visibility = View.GONE
                    nextCursor = paging.nextCursor
                    val hasMoreItem = paging.nextCursor != null
                    if(hasMoreItem) {
                        val cursor = nextCursor?.toInt() ?: -1
                        if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                            promoAdapter?.removeLoadingFooter()
                            promoAdapter?.addList(paging.items.toMutableList())
                            isLoadNextItem = false
                        } else { // first fetch
                            setupAdapter(paging.items)
                        }
                    } else {
                        if(isLoadNextItem) {
                            promoAdapter?.removeLoadingFooter()
                            promoAdapter?.addList(paging.items.toMutableList())
                            isLoadNextItem = false
                        } else {
                            setupAdapter(paging.items)
                        }
                    }
                } else {
                    binding.rvPromo.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAdapter(items: List<Promo>) {
        promoAdapter = PromoAdapter(
            itemList = items.toMutableList(),
            onItemClickListener = object : PromoAdapter.OnItemClickListener {
                override fun onClick(item: Promo) {
                    // TODO to webview?
                }
            }
        )
        binding.rvPromo.adapter = promoAdapter
    }

    companion object {
        const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}