package com.mib.feature_home.contents.bottom_menu.profile

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.R
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib.mvvm.utils.DialogUtils
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.DialogListener
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val sessionRepository: SessionRepository,
    val loadingDialogNavigation: LoadingDialogNavigation
) : BaseViewModel<ProfileViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun isLoggedIn(): Boolean {
        return !sessionRepository.getAccessToken().isNullOrBlank()
    }

    fun logout(navController: NavController) {
        clearLocalSession()
        goToHomeScreen(navController)
    }

    fun showUploadOptionDialog(fragment: Fragment) {
        DialogUtils.showDialogWithTwoButtons(
            fragment.context,
            fragment.getString(R.string.shared_res_logout),
            fragment.getString(R.string.profile_are_you_sure_logout),
            fragment.getString(R.string.shared_res_yes_logout),
            fragment.getString(R.string.shared_res_back),
            object : DialogListener {
                override fun onLeftButtonClicked() {
                    logout(fragment.findNavController())
                }
                override fun onRightButtonClicked() {}
            }
        )
    }

    fun goToLoginScreen(navController: NavController) {
        homeNavigation.goToLoginScreen(navController = navController)
    }

    private fun goToHomeScreen(navController: NavController) {
        homeNavigation.goToHomeScreen(navController = navController)
    }

    private fun clearLocalSession() {
        sessionRepository.clearLocalSession()
    }

    data class ViewState(
        var event: Int? = null,
    ) : BaseViewState
}