package com.mib.sage_user.navigation

import androidx.navigation.NavController
import com.mib.lib_navigation.ProfileNavigation
import com.mib.sage_user.R

class ProfileNavigationImpl : ProfileNavigation {
    override fun goToHomeScreen(navController: NavController) {
        navController.popBackStack(R.id.nav_graph, true)
        navController.navigate(R.id.action_homeFragment)
    }
}