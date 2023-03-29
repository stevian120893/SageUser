package com.mib.feature_home.contents.bottom_menu.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.mib.feature_home.contents.bottom_menu.home.HomeViewModel
import com.mib.feature_home.databinding.FragmentHomeBinding
import com.mib.feature_home.databinding.FragmentLoginBinding
import com.mib.feature_home.databinding.FragmentRegisterBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel>(0) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this@ProfileFragment, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialog.subscribe(this, false)

        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {}

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {}
    }
}