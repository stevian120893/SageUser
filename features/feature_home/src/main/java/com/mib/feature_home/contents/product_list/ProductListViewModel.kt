package com.mib.feature_home.contents.product_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.product_list.ProductListFragment.Companion.KEY_CATEGORY_CODE
import com.mib.feature_home.contents.product_list.ProductListFragment.Companion.KEY_SUBCATEGORY_CODE
import com.mib.feature_home.contents.product_list.ProductListFragment.Companion.KEY_SUBCATEGORY_NAME
import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.usecase.GetProductsUseCase
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
class ProductListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getProductsUseCase: GetProductsUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<ProductListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var categoryCode: String? = null
    private var subcategoryCode: String? = null
    private var subcategoryName: String? = null

    private val _subcategoryNameLiveData = SingleLiveEvent<String>()
    val subcategoryNameLiveData: LiveData<String> = _subcategoryNameLiveData

    fun init(arg: Bundle?) {
        categoryCode = arg?.getString(KEY_CATEGORY_CODE).orEmpty()
        subcategoryCode = arg?.getString(KEY_SUBCATEGORY_CODE).orEmpty()
        subcategoryName = arg?.getString(KEY_SUBCATEGORY_NAME).orEmpty()
        _subcategoryNameLiveData.postValue(arg?.getString(KEY_SUBCATEGORY_NAME).orEmpty())
    }

    fun fetchProducts(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadProducts = true, event = NO_EVENT)
        viewModelScope.launch(ioDispatcher) {
            val result = getProductsUseCase(nextCursor, categoryCode.orEmpty(), subcategoryCode.orEmpty(), "")

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        isLoadProducts = false,
                        event = EVENT_UPDATE_PRODUCT_LIST,
                        productsItemPaging = result.first
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

    fun goToHomeScreen(navController: NavController) {
        homeNavigation.goToHomeScreen(navController)
    }

    data class ViewState(
        var isLoadProducts: Boolean = false,
        var productsItemPaging: ProductsItemPaging? = null,
        var subcategoryName: String? = null,
        var event: Int = NO_EVENT
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 0
        const val EVENT_UPDATE_PRODUCT_LIST = 1
    }
}