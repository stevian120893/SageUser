package com.mib.feature_home.contents.order_history_detail

import android.os.Bundle
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OrderHistoryDetailViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<OrderHistoryDetailViewModel.ViewState>(ViewState(NO_EVENT)) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
//    private var productCode: String? = null

    fun init(arg: Bundle?) {
//        productCode = arg?.getString(KEY_PRODUCT_CODE).orEmpty()
    }

//    fun getProductDetail(navController: NavController) {
//        state = state.copy(isLoadProduct = true, event = EVENT_UPDATE_PRODUCT)
//        viewModelScope.launch(ioDispatcher) {
//            val result = getProductDetailUseCase(productCode.orEmpty())
//
//            withContext(mainDispatcher) {
//                result.first?.let {
//                    state = state.copy(
//                        event = EVENT_UPDATE_PRODUCT,
//                        isLoadProduct = false,
//                        productDetail = it
//                    )
//                }
//                result.second?.let {
//                    toastEvent.postValue(it)
//                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
//                        withContext(mainDispatcher) {
//                            unauthorizedErrorNavigation.handleErrorMessage(navController, it)
//                        }
//                    }
//                }
//            }
//        }
//    }

//    fun bookOrder(fragment: Fragment) {
//        loadingDialog.show()
//        viewModelScope.launch(ioDispatcher) {
//            val result = bookOrderUseCase(productCode.orEmpty(), "address", "12314123", "")
//
//            withContext(mainDispatcher) {
//                loadingDialog.dismiss()
//                result.first?.let {
//                    state = state.copy(event = EVENT_ORDER_SUCCEED)
//                }
//                result.second?.let {
//                    toastEvent.postValue(it)
//                }
//            }
//        }
//    }

    data class ViewState(
        val event: Int,
//        var isLoadProduct: Boolean = false,
//        var productDetail: ProductDetail? = null
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 1
        const val EVENT_UPDATE_PRODUCT = 2
        const val EVENT_ORDER_SUCCEED = 3
    }
}