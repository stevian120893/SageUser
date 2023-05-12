package com.mib.feature_home.contents.bottom_menu.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.databinding.FragmentProfileBinding
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel>(0) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialogNavigation.subscribe(this, false)

        if(viewModel.isLoggedIn()) {
            binding.btLogout.visibility = View.VISIBLE
        } else {
            binding.btLogin.visibility = View.VISIBLE
        }

        lifecycleScope.launch {
            initListener()
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        binding.btLogin.setOnClickListener {
            viewModel.goToLoginScreen(findNavController())
        }
        binding.btLogout.setOnClickListener {
            viewModel.showUploadOptionDialog(this@ProfileFragment)
        }
    }

    private fun observeLiveData(context: Context) {}
}