package com.mib.feature_home.contents.order_history_detail

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mib.feature_home.R
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_ORDER_ID
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_PAYMENT_METHOD_CASH
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_PAYMENT_METHOD_DANA
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_PAYMENT_METHOD_TRANSFER
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.usecase.GetOrderDetailUseCase
import com.mib.feature_home.usecase.PayDanaUseCase
import com.mib.feature_home.usecase.PayTransferUseCase
import com.mib.feature_home.usecase.SendRatingUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
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
class OrderHistoryDetailViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val sendRatingUseCase: SendRatingUseCase,
    private val payDanaUseCase: PayDanaUseCase,
    private val payTransferUseCase: PayTransferUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<OrderHistoryDetailViewModel.ViewState>(ViewState(NO_EVENT)) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private var orderId: String? = null

    fun init(arg: Bundle?) {
        orderId = arg?.getString(KEY_ORDER_ID)
    }

    fun getOrderDetail(navController: NavController) {
        state = state.copy(isLoadOrderDetail = true, event = EVENT_UPDATE_ORDER_DETAIL)
        viewModelScope.launch(ioDispatcher) {
            val result = getOrderDetailUseCase(orderId.orEmpty())

            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        event = EVENT_UPDATE_ORDER_DETAIL,
                        isLoadOrderDetail = false,
                        orderDetail = it
                    )
                }
                result.second?.let {
                    toastEvent.postValue(it)
                    if(it == ApiConstants.ERROR_MESSAGE_UNAUTHORIZED) {
                        withContext(mainDispatcher) {
                            unauthorizedErrorNavigation.handleErrorMessage(navController, it)
                        }
                    }
                }
            }
        }
    }

    fun payOrder(context: Context) {
        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            if(state.orderDetail?.code.isNullOrEmpty()) return@launch

            when(state.orderDetail?.usedPaymentMethod) {
                KEY_PAYMENT_METHOD_DANA -> {
                    val result = payDanaUseCase(orderId.orEmpty())
                    withContext(mainDispatcher) {
                        loadingDialog.dismiss()
                        result.first?.let {
                            AppUtils.goToWebView(context, KEY_PAYMENT_METHOD_DANA, it.paymentUrl, "")
                        }
                        result.second?.let {
                            toastEvent.postValue(it)
                        }
                    }
                }
                KEY_PAYMENT_METHOD_TRANSFER -> {
                    val result = payTransferUseCase(orderId.orEmpty(), "")
                    withContext(mainDispatcher) {
                        loadingDialog.dismiss()
                        result.first?.let {
                            // TODO finish payment
                            state = state.copy(event = EVENT_SEND_RATING)
                        }
                        result.second?.let {
                            toastEvent.postValue(it)
                        }
                    }
                }
            }
        }
    }

    fun sendRating(review: String, rating: String) {
        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            if(orderId.isNullOrEmpty()) return@launch

            val result = sendRatingUseCase(orderId.orEmpty(), rating, review)
            withContext(mainDispatcher) {
                loadingDialog.dismiss()
                result.first?.let {
                    state = state.copy(event = EVENT_SEND_RATING)
                }
                result.second?.let {
                    toastEvent.postValue(it)
                }
            }
        }
    }

    data class ViewState(
        val event: Int,
        var isLoadOrderDetail: Boolean = false,
        var orderDetail: OrderDetail? = null
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 1
        const val EVENT_UPDATE_ORDER_DETAIL = 2
        const val EVENT_ORDER_SUCCEED = 3
        const val EVENT_SEND_RATING = 4
    }
}