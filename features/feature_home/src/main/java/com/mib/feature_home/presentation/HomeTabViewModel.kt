package com.mib.feature_home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.mib.lib.mvvm.NavigationEmitter
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_pref.SessionPref
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @IODispatcher private val ioDispatcher: CoroutineContext,
    private val sessionPref: SessionPref,
    private val sessionRepository: SessionRepository,
    private val homeNavigation: HomeNavigation
) : ViewModel(), NavigationEmitter {

    private var appUpdateType: Int? = null

    override val navigationEvent: SingleLiveEvent<Int> = SingleLiveEvent()

    var backPressedOnce = false

    val backStack: HomeBackStack
    private var stack: List<Int>
        set(value) {
            savedStateHandle[KEY_STACK] = value
        }
        get() = savedStateHandle[KEY_STACK] ?: emptyList()

    init {
        backStack = HomeBackStack(stack)
    }

//    fun checkAppUpdate() {
//        viewModelScope.launch(ioDispatcher) {
//            appUpdateType = appUpdateHandler.checkForUpdateType()
//            appUpdateType?.let {
//                appUpdateHandler.updateLastUpdateSheetShown()
//                navigationEvent.postValue(NAVIGATION_DIALOG_UPDATE)
//            }
//        }
//    }

//    fun showUpdateDialog(navController: NavController) {
//        val type = appUpdateType ?: return
//        miscNavigation.showUpdateDialog(navController, type)
//    }
//
//    fun requestUpdateApp(activity: Activity) {
//        val type = appUpdateType ?: return
//        viewModelScope.launch(ioDispatcher) {
//            appUpdateHandler.requestUpdate(activity, type)
//        }
//    }

    fun isLoggedIn(navController: NavController): Boolean {
        return if(sessionRepository.getAccessToken().isNullOrBlank()) {
            homeNavigation.goToLoginScreen(navController)
            false
        } else true
    }

    fun saveBackStack() {
        stack = backStack.getStack()
    }

//    fun checkUpdateUserConsent() {
//        viewModelScope.launch(ioDispatcher) {
//            val (shouldShowUpdateConsent, _) = checkUpdateUserConsentUseCase()
//            if (shouldShowUpdateConsent) {
//                // TODO: handle update consent
//            }
//        }
//    }

    companion object {
        private const val KEY_STACK = "stack"

        const val NAVIGATION_DIALOG_UPDATE = 1
    }
}
