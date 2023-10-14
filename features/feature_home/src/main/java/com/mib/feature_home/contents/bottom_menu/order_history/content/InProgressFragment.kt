package com.mib.feature_home.contents.bottom_menu.order_history.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mib.feature_home.adapter.OrderHistoryAdapter
import com.mib.feature_home.databinding.FragmentContentInProgressBinding
import com.mib.feature_home.domain.model.OrderHistory
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InProgressFragment : BaseFragment<InProgressViewModel>(0) {
    private var _binding: FragmentContentInProgressBinding? = null
    private val binding get() = _binding!!

    private var orderHistoryAdapter: OrderHistoryAdapter? = null
    private var isLoadNextItem = false
    private var nextCursor: String? = null

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(InProgressViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@InProgressFragment, backPressedCallback)
        if(viewModel.isLoggedIn()) viewModel.getOrderHistory(this@InProgressFragment, DEFAULT_NEXT_CURSOR_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentInProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)

        lifecycleScope.launch {
            AppUtils.firstSetRecyclerView(view.context, LinearLayoutManager.VERTICAL, binding.rvHistory)
            if(!viewModel.isLoggedIn()) binding.llLogin.visibility = View.VISIBLE

            observeLiveData()
            initListener()
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            if(state.isLoadHistory) {
                binding.llContent.visibility = View.GONE
                binding.sflHistory.visibility = View.VISIBLE
            } else {
                if (binding.srlHistory.isRefreshing) binding.srlHistory.isRefreshing = false
                binding.sflHistory.visibility = View.GONE
                binding.llContent.visibility = View.VISIBLE
                state.orderHistoryItemPaging?.let { paging ->
                    if(paging.items?.isNotEmpty() == true) {
                        binding.rvHistory.visibility = View.VISIBLE
                        binding.llNoData.visibility = View.GONE
                        nextCursor = paging.nextCursor
                        val hasMoreItem = paging.nextCursor != null
                        if(hasMoreItem) {
                            val cursor = nextCursor?.toInt() ?: -1
                            if (cursor > DEFAULT_NEXT_CURSOR_RESPONSE) {
                                orderHistoryAdapter?.removeLoadingFooter()
                                orderHistoryAdapter?.addList(paging.items.toMutableList())
                                isLoadNextItem = false
                            } else { // first fetch
                                setupAdapter(paging.items)
                            }
                        } else {
                            if(isLoadNextItem) {
                                orderHistoryAdapter?.removeLoadingFooter()
                                isLoadNextItem = false
                            } else {
                                setupAdapter(paging.items)
                            }
                        }
                    } else {
                        binding.rvHistory.visibility = View.GONE
                        binding.llNoData.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val size = orderHistoryAdapter?.itemList?.size ?: -1
                if (!isLoadNextItem && size >= MAX_PAGINATION_ITEMS) {
                    if (AppUtils.isLastItemDisplaying(recyclerView)) {
                        nextCursor?.let {
                            viewModel.getOrderHistory(this@InProgressFragment, nextCursor.orEmpty())
                            orderHistoryAdapter?.addLoadingFooter()
                            isLoadNextItem = true
                        }
                    }
                }
            }
        })

        binding.llLogin.setOnClickListener {
            viewModel.goToLoginScreen(findNavController())
        }

        binding.srlHistory.setOnRefreshListener {
            viewModel.getOrderHistory(this@InProgressFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }

        binding.llNoData.setOnClickListener {
            viewModel.getOrderHistory(this@InProgressFragment, DEFAULT_NEXT_CURSOR_REQUEST)
        }
    }

    private fun setupAdapter(items: List<OrderHistory>) {
        val itemsFiltered = items.filter { it.status != OrderDetail.DONE && it.status != OrderDetail.CANCEL }
        orderHistoryAdapter = OrderHistoryAdapter(
            itemList = itemsFiltered.toMutableList(),
            onItemClickListener = object : OrderHistoryAdapter.OnItemClickListener {
                override fun onClick(item: OrderHistory) {
                    viewModel.goToOrderDetailScreen(findNavController(), item.code)
                }
            }
        )
        binding.rvHistory.adapter = orderHistoryAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MAX_PAGINATION_ITEMS = 10
        private const val DEFAULT_NEXT_CURSOR_REQUEST = "1"
        private const val DEFAULT_NEXT_CURSOR_RESPONSE = 2
    }
}