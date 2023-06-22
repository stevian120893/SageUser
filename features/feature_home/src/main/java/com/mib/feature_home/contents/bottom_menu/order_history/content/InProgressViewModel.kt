package com.mib.feature_home.contents.bottom_menu.order_history.content

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.domain.model.OrderHistoryItemPaging
import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.usecase.GetOrderHistoryUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_auth.usecase.LoginUseCase
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class InProgressViewModel @Inject constructor(
    val loadingDialog: LoadingDialogNavigation,
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val sessionRepository: SessionRepository,
    private val getOrderHistoryUseCase: GetOrderHistoryUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    private val homeNavigation: HomeNavigation
) : BaseViewModel<InProgressViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun getOrderHistory(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadHistory = true)
        viewModelScope.launch(ioDispatcher) {
            val result = getOrderHistoryUseCase(nextCursor)

            withContext(mainDispatcher) {
                result.first?.items?.let {
                    state = state.copy(
                        isLoadHistory = false,
                        orderHistoryItemPaging = result.first
                    )
                }
                result.second?.let {
                    toastEvent.postValue(it)
                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                        withContext(mainDispatcher) {
                            unauthorizedErrorNavigation.handleErrorMessage(fragment.findNavController(), it)
                        }
                    }
                }
            }
        }
    }

    fun goToLoginScreen(navController: NavController) {
        homeNavigation.goToLoginScreen(navController = navController)
    }

    fun goToOrderDetailScreen(navController: NavController, orderId: String) {
        homeNavigation.goToOrderDetailScreen(navController = navController, orderId = orderId)
    }

    fun isLoggedIn(): Boolean {
        return !sessionRepository.getAccessToken().isNullOrBlank()
    }

    data class ViewState(
        var isLoadHistory: Boolean = false,
        var orderHistoryItemPaging: OrderHistoryItemPaging? = null
    ) : BaseViewState
}