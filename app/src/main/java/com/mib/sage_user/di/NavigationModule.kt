package com.mib.sage_user.di

import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_navigation.UnauthorizedErrorNavigation
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.sage_user.navigation.UnauthorizedErrorNavigationImpl
import com.mib.feature_home.navigation.HomeNavigationImpl
import com.mib.sage_user.navigation.LoadingDialogNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    fun provideHomeNavigation(): HomeNavigation = HomeNavigationImpl()

    @Provides
    fun provideLoadingDialogNavigation(): LoadingDialogNavigation = LoadingDialogNavigationImpl()

    @Provides
    fun provideUnauthorizedErrorNavigation(
        sessionRepository: SessionRepository,
    ): UnauthorizedErrorNavigation {
        return UnauthorizedErrorNavigationImpl(sessionRepository)
    }
}
