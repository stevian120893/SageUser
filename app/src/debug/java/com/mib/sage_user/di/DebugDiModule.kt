package com.mib.sage_user.di

import com.mib.sage_user.config.ApiAppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DebugDiModule {
    @Provides
    @BaseUrlQualifier
    fun provideBaseUrl(): String {
        return ApiAppConfig.PREPRODUCTION_BASE_URL
    }
}
