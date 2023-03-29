package com.mib.lib_navigation

import androidx.navigation.NavController

interface HomeNavigation {
    fun goToHomeScreen(navController: NavController)
    fun goToCategoryListScreen(navController: NavController)
    fun goToSubcategoryListScreen(navController: NavController, categoryCode: String, categoryName: String)
    fun goToProductListScreen(navController: NavController, categoryCode: String, subcategoryCode: String, subcategoryName: String)
}