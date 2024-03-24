package com.mib.feature_home.contents.bottom_menu.order_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.mib.feature_home.R
import com.mib.feature_home.contents.bottom_menu.order_history.content.CompletedFragment
import com.mib.feature_home.contents.bottom_menu.order_history.content.InProgressFragment
import com.mib.feature_home.databinding.FragmentOrderHistoryBinding
import com.mib.feature_home.utils.TabAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {

    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@OrderHistoryFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.vpHistory.currentItem = 0
    }

    private fun initUi() {
        val adapter = TabAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(InProgressFragment(), getString(R.string.active))
        adapter.addFragment(CompletedFragment(), getString(R.string.done))
        binding.tabLayout.setupWithViewPager(binding.vpHistory, adapter)
        binding.vpHistory.isUserInputEnabled = false

        binding.vpHistory.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when(position) {
                    0 -> {
                        val fragment = childFragmentManager.findFragmentByTag("f$position") as? InProgressFragment
                        fragment?.refreshFragment()
                    }
                    else -> {
                        val fragment = childFragmentManager.findFragmentByTag("f$position") as? CompletedFragment
                        fragment?.refreshFragment()
                    }
                }
            }
        })

    }
}