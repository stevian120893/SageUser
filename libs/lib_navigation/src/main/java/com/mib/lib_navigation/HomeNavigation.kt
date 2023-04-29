package com.mib.lib_navigation

import androidx.navigation.NavController

interface HomeNavigation {
    fun goToHomeScreen(navController: NavController)
    fun goToCategoryListScreen(navController: NavController)
    fun goToSubcategoryListScreen(navController: NavController, categoryCode: String, categoryName: String)
    fun goToProductListScreen(navController: NavController, categoryCode: String, subcategoryCode: String, subcategoryName: String)
    fun goToProductDetailScreen(navController: NavController, productCode: String)
    fun goToLoginScreen(navController: NavController)
    fun goToRegisterScreen(navController: NavController)
}