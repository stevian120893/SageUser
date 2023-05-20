package com.mib.feature_home.contents.subcategory_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.subcategory_list.SubcategoryListFragment.Companion.KEY_CATEGORY_CODE
import com.mib.feature_home.contents.subcategory_list.SubcategoryListFragment.Companion.KEY_CATEGORY_NAME
import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.usecase.GetSubcategoriesUseCase
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
class SubcategoryListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getSubcategoriesUseCase: GetSubcategoriesUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<SubcategoryListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var categoryCode: String? = null
    private val _categoryCodeLiveData = SingleLiveEvent<String>()
    val categoryCodeLiveData: LiveData<String> = _categoryCodeLiveData

    fun init(arg: Bundle?) {
        categoryCode = arg?.getString(KEY_CATEGORY_CODE).orEmpty()
        _categoryCodeLiveData.postValue(arg?.getString(KEY_CATEGORY_NAME).orEmpty())
    }

    fun fetchSubcategories(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadSubcategories = true, event = NO_EVENT)
        viewModelScope.launch(ioDispatcher) {
            val result = getSubcategoriesUseCase(nextCursor, categoryCode)

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        isLoadSubcategories = false,
                        event = EVENT_UPDATE_LIST,
                        subcategoriesItemPaging = result.first
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

    fun goToProductListScreen(
        navController: NavController,
        categoryCode: String? = null,
        subcategoryCode: String? = null,
        subcategoryName: String? = null,
        isSearch: Boolean = false
    ) {
        homeNavigation.goToProductListScreen(
            navController,
            categoryCode,
            subcategoryCode,
            subcategoryName,
            isSearch
        )
    }

    data class ViewState(
        var isLoadSubcategories: Boolean? = null,
        var subcategoriesItemPaging: SubcategoriesItemPaging? = null,
        var categoryName: String? = null,
        var event: Int = NO_EVENT
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 0
        const val EVENT_UPDATE_LIST = 1
    }
}