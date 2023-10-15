package com.mib.feature_home.contents.order_history_detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_IS_FROM_CHECKOUT
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_ORDER_ID
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_PAYMENT_METHOD_DANA
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_PAYMENT_METHOD_TRANSFER
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.interfaces.GiveRatingListener
import com.mib.feature_home.usecase.GetOrderDetailUseCase
import com.mib.feature_home.usecase.PayDanaUseCase
import com.mib.feature_home.usecase.PayTransferUseCase
import com.mib.feature_home.usecase.SendRatingUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.DialogUtils
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import pl.aprilapps.easyphotopicker.EasyImage
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class OrderHistoryDetailViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val sendRatingUseCase: SendRatingUseCase,
    private val payDanaUseCase: PayDanaUseCase,
    private val payTransferUseCase: PayTransferUseCase,
    private val homeNavigation: HomeNavigation,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<OrderHistoryDetailViewModel.ViewState>(ViewState(NO_EVENT)) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private var orderId: String? = null
    private var isFromCheckout: Boolean = false

    fun init(arg: Bundle?) {
        orderId = arg?.getString(KEY_ORDER_ID)
        isFromCheckout = arg?.getBoolean(KEY_IS_FROM_CHECKOUT) ?: false
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

    fun payOrder(context: Context, paymentReceiptImage: MultipartBody.Part? = null) {
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
                    val result = payTransferUseCase(
                        code = orderId.orEmpty(),
                        paymentReceiptImage = paymentReceiptImage
                    )
                    withContext(mainDispatcher) {
                        loadingDialog.dismiss()
                        result.first?.let {
                            // TODO finish payment after upload payment receipt
                        }
                        result.second?.let {
                            toastEvent.postValue(it)
                        }
                    }
                }
            }
        }
    }

    fun showRatingDialog(context: Context, navController: NavController) {
        DialogUtils.showGiveReviewDialog(
            context = context,
            object : GiveRatingListener {
                override fun sendRating(rating: String, review: String) {
                    sendOrderRating(navController, rating, review)
                }
            }
        )
    }

    fun showUploadOptionDialog(fragment: Fragment, easyImage: EasyImage) {
        mediaEvent.postValue(fragment to easyImage)
    }

    fun onBackPressed(fragment: Fragment) {
        if(isFromCheckout) homeNavigation.goToHomeScreen(fragment.findNavController())
        else fragment.findNavController().popBackStack()
    }

    private fun sendOrderRating(navController: NavController, review: String, rating: String) {
        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            if(orderId.isNullOrEmpty()) return@launch

            val result = sendRatingUseCase(orderId.orEmpty(), rating, review)
            withContext(mainDispatcher) {
                loadingDialog.dismiss()
                result.first?.let {
                    getOrderDetail(navController)
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
    }
}