package com.mib.feature_home.contents.product_detail

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.product_detail.ProductDetailFragment.Companion.KEY_PRODUCT_CODE
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.usecase.GetProductDetailUseCase
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
class ProductDetailViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getProductDetailUseCase: GetProductDetailUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<ProductDetailViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private var productCode: String? = null

    fun init(arg: Bundle?) {
        productCode = arg?.getString(KEY_PRODUCT_CODE).orEmpty()
    }

    fun getProductDetail(navController: NavController) {
        state = state.copy(isLoadProduct = true)
        viewModelScope.launch(ioDispatcher) {
            val result = getProductDetailUseCase(productCode.orEmpty())

            loadingDialog.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
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

    data class ViewState(
        var isLoadProduct: Boolean = false,
        var productDetail: ProductDetail? = null
    ) : BaseViewState
}