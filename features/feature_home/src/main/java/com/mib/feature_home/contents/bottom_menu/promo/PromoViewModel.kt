package com.mib.feature_home.contents.bottom_menu.promo

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.usecase.GetPromoUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class PromoViewModel @Inject constructor(
    val loadingDialog: LoadingDialogNavigation,
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val getPromoUseCase: GetPromoUseCase,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation,
    private val sessionRepository: SessionRepository
) : BaseViewModel<PromoViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchPromo(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadPromo = true)
        viewModelScope.launch(ioDispatcher) {
            val result = getPromoUseCase(nextCursor)

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        isLoadPromo = false,
                        promoItemPaging = result.first
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

    fun isLoggedIn(): Boolean {
        return !sessionRepository.getAccessToken().isNullOrBlank()
    }

    data class ViewState(
        var isLoadPromo: Boolean = false,
        var promoItemPaging: PromoItemPaging? = null,
    ) : BaseViewState
}
