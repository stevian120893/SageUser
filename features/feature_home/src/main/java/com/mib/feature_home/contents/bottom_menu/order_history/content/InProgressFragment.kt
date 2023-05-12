package com.mib.feature_home.contents.bottom_menu.order_history.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.mib.feature_home.databinding.FragmentContentInProgressBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InProgressFragment : BaseFragment<InProgressViewModel>(0) {
    private var _binding: FragmentContentInProgressBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}