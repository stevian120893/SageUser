package com.mib.sage_user.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.mib.feature_home.contents.order_history_detail.OrderHistoryDetailFragment.Companion.KEY_ORDER_ID
import com.mib.feature_home.contents.product_detail.ProductDetailFragment.Companion.KEY_PRODUCT_CODE
import com.mib.feature_home.contents.product_list.ProductListFragment
import com.mib.feature_home.contents.subcategory_list.SubcategoryListFragment.Companion.KEY_CATEGORY_CODE
import com.mib.feature_home.contents.subcategory_list.SubcategoryListFragment.Companion.KEY_CATEGORY_NAME
import com.mib.lib_navigation.HomeNavigation
import com.mib.sage_user.R

class HomeNavigationImpl : HomeNavigation {
    override fun goToHomeScreen(navController: NavController) {
        navController.navigate(R.id.action_homeFragment)
    }
    override fun goToCategoryListScreen(
        navController: NavController,
        categoryCode: String?
    ) {
        navController.navigate(
            R.id.action_categoryListFragment,
            bundleOf(
                KEY_CATEGORY_CODE to categoryCode,
            )
        )
    }
    override fun goToSubcategoryListScreen(
        navController: NavController,
        categoryCode: String,
        categoryName: String
    ) {
        navController.navigate(
            R.id.action_subcategoryListFragment,
            bundleOf(
                KEY_CATEGORY_CODE to categoryCode,
                KEY_CATEGORY_NAME to categoryName
            )
        )
    }
    override fun goToProductListScreen(
        navController: NavController,
        categoryCode: String?,
        subcategoryCode: String?,
        subcategoryName: String?,
        isSearch: Boolean
    ) {
        navController.navigate(
            R.id.action_productListFragment,
            bundleOf(
                ProductListFragment.KEY_CATEGORY_CODE to categoryCode,
                ProductListFragment.KEY_SUBCATEGORY_CODE to subcategoryCode,
                ProductListFragment.KEY_SUBCATEGORY_NAME to subcategoryName,
                ProductListFragment.KEY_IS_SEARCH to isSearch
            )
        )
    }

    override fun goToProductDetailScreen(
        navController: NavController,
        productCode: String
    ) {
        navController.navigate(
            R.id.action_productDetailFragment,
            bundleOf(
                KEY_PRODUCT_CODE to productCode,
            )
        )
    }

    override fun goToLoginScreen(navController: NavController) {
        navController.navigate(R.id.action_login_fragment)
    }

    override fun goToRegisterScreen(navController: NavController) {
        navController.navigate(R.id.action_register_fragment)
    }

    override fun goToOrderDetailScreen(
        navController: NavController,
        orderId: String
    ) {
        navController.navigate(
            R.id.action_orderDetailFragment,
            bundleOf(
                KEY_ORDER_ID to orderId,
            )
        )
    }
}