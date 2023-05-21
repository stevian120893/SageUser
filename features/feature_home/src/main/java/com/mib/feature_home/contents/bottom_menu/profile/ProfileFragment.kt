package com.mib.feature_home.contents.bottom_menu.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mib.feature_home.R
import com.mib.feature_home.databinding.FragmentProfileBinding
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.DialogUtils
import com.mib.lib.mvvm.BaseFragment
import com.mib.lib_navigation.DialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel>(0) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(viewModel.isLoggedIn()) viewModel.getProfile(findNavController())
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
            binding.llSignout.visibility = View.VISIBLE
            binding.ivIconProfile.clipToOutline = true
        } else {
            binding.llSignIn.visibility = View.VISIBLE
        }

        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        binding.llSupport.setOnClickListener {
            DialogUtils.showDialogWithTwoButtons(
                context = context,
                title = context.getString(R.string.contact),
                subtitle = context.getString(R.string.contact_us_via),
                left = context.getString(R.string.whatsapp),
                right = context.getString(R.string.email),
                object : DialogListener {
                    override fun onLeftButtonClicked() {
                        AppUtils.openWhatsApp(context, context.getString(R.string.shared_res_whatsapp_number))
                    }

                    override fun onRightButtonClicked() {
                        AppUtils.openEmail(context, context.getString(R.string.contact_us_email))
                    }
                }
            )
        }

        binding.llTermsAndConditions.setOnClickListener {
//            AppUtils.goToWebView(context, getString(R.string.profile_terms_and_conditions), SharedPreferenceHelper.getTncLink(context!!), "")
        }

        binding.llRateUs.setOnClickListener {
            AppUtils.launchMarket(context)
        }

        binding.llSignIn.setOnClickListener {
            viewModel.goToLoginScreen(findNavController())
        }
        binding.llSignout.setOnClickListener {
            viewModel.showUploadOptionDialog(this@ProfileFragment)
        }
    }

    private fun observeLiveData(context: Context) {
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            binding.tvName.text = it.profile?.name.orEmpty()
            Glide.with(context).load(it.profile?.profilePictureImageUrl).into(binding.ivIconProfile)
        }
    }
}