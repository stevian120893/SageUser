package com.mib.feature_home.contents.product_detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mib.feature_home.R
import com.mib.feature_home.`interface`.DialogOrderListener
import com.mib.feature_home.contents.product_detail.ProductDetailFragment.Companion.KEY_PRODUCT_CODE
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.usecase.BookOrderUseCase
import com.mib.feature_home.usecase.GetProductDetailUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.DialogUtils
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_api.ApiConstants
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.DialogListener
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
class ProductDetailViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val bookOrderUseCase: BookOrderUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<ProductDetailViewModel.ViewState>(ViewState(NO_EVENT)) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private var productCode: String? = null

    fun init(arg: Bundle?) {
        productCode = arg?.getString(KEY_PRODUCT_CODE).orEmpty()
    }

    fun getProductDetail(navController: NavController) {
        state = state.copy(isLoadProduct = true, event = EVENT_UPDATE_PRODUCT)
        viewModelScope.launch(ioDispatcher) {
            val result = getProductDetailUseCase(productCode.orEmpty())

            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        event = EVENT_UPDATE_PRODUCT,
                        isLoadProduct = false,
                        productDetail = it
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

    fun confirmOrder(context: Context) {
        DialogUtils.showOrderConfirmationDialog(
            context = context,
            object : DialogOrderListener {
                override fun order(bookingDate: String, bookingTime: String, bookingAddress: String, bookingNote: String) {
                    if(bookingDate.isEmpty()) {
                        toastEvent.postValue(context.getString(R.string.shared_res_please_fill_blank_space))
                        return
                    }

                    val bookingDateAndTime = "$bookingDate $bookingTime"
                    val timeInMillis = AppUtils.convertDateToMillis(bookingDateAndTime)
                    if(timeInMillis.isEmpty()) {
                        toastEvent.postValue(context.getString(R.string.shared_res_please_fill_blank_space))
                        return
                    }
                    bookOrder(timeInMillis, bookingAddress, bookingNote)
                }
            }
        )
    }

    private fun bookOrder(bookingDate: String, bookingAddress: String, bookingNote: String) {
        loadingDialog.show()
        viewModelScope.launch(ioDispatcher) {
            val result = bookOrderUseCase(productCode.orEmpty(), bookingAddress, bookingDate, bookingNote)

            withContext(mainDispatcher) {
                loadingDialog.dismiss()
                result.first?.let {
                    state = state.copy(event = EVENT_ORDER_SUCCEED)
                }
                result.second?.let {
                    toastEvent.postValue(it)
                }
            }
        }
    }

    data class ViewState(
        val event: Int,
        var isLoadProduct: Boolean = false,
        var productDetail: ProductDetail? = null
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 1
        const val EVENT_UPDATE_PRODUCT = 2
        const val EVENT_ORDER_SUCCEED = 3
    }
}