package com.mib.feature_home.contents.register

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.feature_home.usecase.RegisterUseCase
import com.mib.feature_home.usecase.auth.SendCodeUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.AppUtils.Companion.KEY_HASH_PASSWORD
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val registerUseCase: RegisterUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<RegisterViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var passwordHash: String? = null
    private var gender: String? = null

    fun updateGender(gender: String) {
        this.gender = gender
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun register(
        fragment: Fragment,
        email: String,
        password: String,
        name: String,
        phone: String,
        code: String,
        profileFile: MultipartBody.Part?
    ) {
        if(!isFormValid(email, password, name, phone, gender, code)) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            passwordHash?.let {
                val result = registerUseCase(
                    email,
                    it,
                    name,
                    phone,
                    gender.orEmpty(),
                    profileFile,
                    code
                )

                loadingDialogNavigation.dismiss()
                withContext(mainDispatcher) {
                    result.first?.let {
                        toastEvent.postValue(fragment.context?.getString(R.string.register_succeed))
                        goToHomeScreen(fragment.findNavController())
                    }
                    result.second?.let { errMsg ->
                        toastEvent.postValue(errMsg)
                    }
                }
            }
        }
    }

    fun sendCode(fragment: Fragment, email: String) {
        if(email.isBlank()) {
            toastEvent.postValue(fragment.context?.getString(R.string.shared_res_please_fill_blank_space))
            return
        }

        loadingDialogNavigation.show()
        viewModelScope.launch(ioDispatcher) {
            val result = sendCodeUseCase(email)

            loadingDialogNavigation.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    toastEvent.postValue(fragment.context?.getString(R.string.register_send_code_succeed))
                }
                result.second?.let {
                    toastEvent.postValue(it)
                }
            }
        }
    }

    private fun goToHomeScreen(navController: NavController) {
        homeNavigation.goToHomeScreen(
            navController = navController
        )
    }

    fun goToLoginScreen(navController: NavController) {
        homeNavigation.goToLoginScreen(
            navController = navController
        )
    }

    private fun isFormValid(
        email: String,
        password: String? = null,
        name: String? = null,
        phone: String? = null,
        gender: String? = null,
        code: String? = null
    ): Boolean {
        if(!password.isNullOrBlank()) {
            passwordHash = AppUtils.encode(KEY_HASH_PASSWORD, password)
        } else {
            return false
        }

        return email.isNotBlank() && name?.isNotBlank() == true && phone?.isNotBlank() == true && gender?.isNotBlank() == true && code?.isNotBlank() == true && !passwordHash.isNullOrBlank()
    }

    data class ViewState(
        var event: Int? = null,
    ) : BaseViewState
}