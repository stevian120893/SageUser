package com.mib.lib_navigation

import androidx.navigation.NavController

interface HomeNavigation {
    fun goToHomeScreen(navController: NavController)
    fun goToCategoryListScreen(
        navController: NavController,
        categoryCode: String?,
        cityCode: String? = null
    )
    fun goToSubcategoryListScreen(
        navController: NavController,
        categoryCode: String,
        categoryName: String
    )
    fun goToProductListScreen(
        navController: NavController,
        categoryCode: String? = null,
        subcategoryCode: String? = null,
        subcategoryName: String? = null,
        isSearch: Boolean = false,
        cityCode: String? = null
    )
    fun goToProductDetailScreen(navController: NavController, productCode: String)
    fun goToLoginScreen(navController: NavController)
    fun goToRegisterScreen(navController: NavController)
    fun goToOrderDetailScreen(navController: NavController, orderId: String)
}