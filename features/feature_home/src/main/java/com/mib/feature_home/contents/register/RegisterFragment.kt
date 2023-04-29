package com.mib.feature_home.contents.register

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.databinding.FragmentRegisterBinding
import com.mib.feature_home.utils.createEasyImage
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterViewModel>(0) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var easyImage: EasyImage
    private var profilePhotoFile: MultipartBody.Part? = null
    private var genderAdapter: ArrayAdapter<String>? = null

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyImage = createEasyImage(view.context)
        viewModel.loadingDialogNavigation.subscribe(this, false)
        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData(view.context)
            initAdapter(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter(context: Context) {
        val array = listOf("M", "P")
        genderAdapter = ArrayAdapter<String>(
            context,
            android.R.layout.simple_spinner_item,
            array
        )
        genderAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.snGender.adapter = genderAdapter
        setGenderSpinnerListener(array)
    }

    private fun initListener(context: Context) {
        binding.btRegister.setOnClickListener {
            viewModel.register(
                fragment = this,
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString(),
                name = binding.etName.text.toString(),
                phone = binding.etPhoneNumber.text.toString(),
                code = binding.etCode.text.toString(),
                profileFile = profilePhotoFile,
            )
        }

        binding.btSendCode.setOnClickListener {
            viewModel.sendCode(this, binding.etEmail.text.toString())
        }

        binding.ivAddPhotoProfile.setOnClickListener {
            viewModel.showUploadOptionDialog(this@RegisterFragment, easyImage)
        }

        var isShown = false
        binding.ivPasswordIndicator.setOnClickListener {
            if(!isShown) {
                binding.ivPasswordIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_password_seen))
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.ivPasswordIndicator.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_password_unseen))
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            binding.etPassword.setSelection(binding.etPassword.length())
            isShown = !isShown
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToLoginScreen(findNavController())
        }
    }

    private fun observeLiveData(context: Context) {}

    private fun setGenderSpinnerListener(genders: List<String>?) {
        binding.snGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.updateGender(genders?.get(position).orEmpty())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            34964, 34962 -> {
                easyImage.handleActivityResult(requestCode, resultCode, data, requireActivity(), object: DefaultCallback() {
                    override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                        lifecycleScope.launchWhenResumed {
                            val compressedFile = Compressor.compress(requireActivity(), imageFiles[0].file) {
                                quality(50)
                            }
                            val myBitmap = BitmapFactory.decodeFile(imageFiles[0].file.absolutePath)
                            binding.ivAddPhotoProfile.setImageBitmap(myBitmap)
                            profilePhotoFile = MultipartBody.Part.createFormData(
                                "profile_image",
                                compressedFile.name,
                                compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            )
                        }
                    }
                })
            }
        }
    }
}