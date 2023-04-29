package com.mib.feature_home.contents.bottom_menu.promo

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.product_list.ProductListViewModel
import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_auth.usecase.LoginUseCase
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

@HiltViewModel
class PromoViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val loginUseCase: LoginUseCase,
    val loadingDialog: LoadingDialogNavigation
) : BaseViewModel<PromoViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchProducts(fragment: Fragment, nextCursor: String? = null) {
//        state = state.copy(isLoadProducts = true, event = ProductListViewModel.NO_EVENT)
//        viewModelScope.launch(ioDispatcher) {
//            val result = getProductsUseCase(nextCursor, categoryCode.orEmpty(), subcategoryCode.orEmpty(), "")
//
//            withContext(mainDispatcher) {
//                result.first.items?.let {
//                    state = state.copy(
//                        isLoadProducts = false,
//                        event = ProductListViewModel.EVENT_UPDATE_PRODUCT_LIST,
//                        productsItemPaging = result.first
//                    )
//                }
//                result.second?.let {
//                    toastEvent.postValue(it)
//                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
//                        withContext(mainDispatcher) {
//                            unauthorizedErrorNavigation.handleErrorMessage(fragment.findNavController(), it)
//                        }
//                    }
//                }
//            }
//        }
    }

    data class ViewState(
        var isLoadProducts: Boolean = false,
        var productsItemPaging: ProductsItemPaging? = null,
        var event: Int = NO_EVENT
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 0
        const val EVENT_UPDATE_PRODUCT_LIST = 1
    }
}