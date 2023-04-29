package com.mib.feature_home.contents.category_list

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.usecase.GetCategoriesUseCase
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
class CategoryListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<CategoryListViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun fetchCategories(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadCategories = true)
        viewModelScope.launch(ioDispatcher) {
            val result = getCategoriesUseCase(nextCursor)

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        isLoadCategories = false,
                        categoriesItemPaging = result.first
                    )
                }
                result.second?.let {
                    state = state.copy(isLoadCategories = false)
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

    fun goToSubcategoryListScreen(
        navController: NavController,
        categoryCode: String,
        categoryName: String
    ) {
        homeNavigation.goToSubcategoryListScreen(
            navController,
            categoryCode,
            categoryName
        )
    }

    data class ViewState(
        var isLoadCategories: Boolean = false,
        var categoriesItemPaging: CategoriesItemPaging? = null
    ) : BaseViewState
}