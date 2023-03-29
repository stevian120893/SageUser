package com.mib.feature_home.contents.bottom_menu.home

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.mib.feature_home.domain.model.Banner
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.usecase.home.GetHomeContentUseCase
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
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
class HomeViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getHomeContentUseCase: GetHomeContentUseCase,
    val loadingDialog: LoadingDialogNavigation
) : BaseViewModel<HomeViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun getHomeContent() {
        viewModelScope.launch(ioDispatcher) {
            val result = getHomeContentUseCase()

            loadingDialog.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        banner = it.banners,
                        category = it.categories
                    )
                }
                result.second?.let {
                    toastEvent.postValue(it)
                }
            }
        }
    }

    fun goToCategoryListScreen(navController: NavController) {
        homeNavigation.goToCategoryListScreen(navController)
    }

    fun goToSubcategoryListScreen(navController: NavController, categoryCode: String, categoryName: String) {
        homeNavigation.goToSubcategoryListScreen(navController, categoryCode, categoryName)
    }

    data class ViewState(
        var banner: List<Banner>? = null,
        var category: List<Category>? = null
    ) : BaseViewState
}