package com.mib.feature_home.contents.category_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.category_list.CategoryListFragment.Companion.KEY_CATEGORY_CODE
import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.usecase.GetCategoriesUseCase
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
class CategoryListViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getSubcategoriesUseCase: GetSubcategoriesUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val unauthorizedErrorNavigation: UnauthorizedErrorNavigation
) : BaseViewModel<CategoryListViewModel.ViewState>(ViewState(event = NO_EVENT)) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    var categoryCode: String? = null

    fun init(arg: Bundle?) {
        updateCategoryCode(arg?.getString(KEY_CATEGORY_CODE))
    }

    fun updateCategoryCode(code: String?) {
        categoryCode = code
    }

    fun updateSelectedIndex(pos: Int) {
        state = state.copy(
            selectedItemIndex = pos,
            lastSelectedItemIndex = pos
        )
    }

    fun fetchCategories(fragment: Fragment) {
        state = state.copy(isLoadCategories = true, event = EVENT_UPDATE_CATEGORY)
        viewModelScope.launch(ioDispatcher) {
            val result = getCategoriesUseCase()

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        isLoadCategories = false,
                        categoriesItemPaging = result.first,
                        event = EVENT_UPDATE_CATEGORY
                    )
                }
                result.second?.let {
                    state = state.copy(isLoadCategories = false, event = EVENT_UPDATE_CATEGORY)
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

    fun fetchSubcategories(fragment: Fragment, nextCursor: String? = null) {
        state = state.copy(isLoadSubcategories = true, event = EVENT_UPDATE_SUBCATEGORY)
        viewModelScope.launch(ioDispatcher) {
            val result = getSubcategoriesUseCase(nextCursor, categoryCode)

            withContext(mainDispatcher) {
                result.first.items?.let {
                    state = state.copy(
                        isLoadSubcategories = false,
                        event = EVENT_UPDATE_SUBCATEGORY,
                        subcategoriesItemPaging = result.first
                    )
                }
                result.second?.let {
                    state = state.copy(isLoadSubcategories = false, event = EVENT_UPDATE_SUBCATEGORY)
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

    fun onClickProductItem(index: Int, category: Category) {
        state = state.copy(
            event = EVENT_SELECT_CATEGORY,
            selectedItem = category,
            selectedItemIndex = index,
            lastSelectedItemIndex = state.selectedItemIndex
        )
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
        var isLoadCategories: Boolean = false,
        val event: Int,
        var categoriesItemPaging: CategoriesItemPaging? = null,
        val lastSelectedItemIndex: Int? = null,
        val selectedItem: Category? = null,
        val selectedItemIndex: Int? = null
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 1
        const val EVENT_UPDATE_CATEGORY = 2
        const val EVENT_UPDATE_SUBCATEGORY = 3
        const val EVENT_SELECT_CATEGORY = 4
    }
}