package com.mib.feature_home.di

import com.mib.feature_home.repository.HomeRepository
import com.mib.feature_home.repository.HomeWithAuthRepository
import com.mib.feature_home.usecase.GetCategoriesUseCase
import com.mib.feature_home.usecase.GetProductDetailUseCase
import com.mib.feature_home.usecase.GetProductsUseCase
import com.mib.feature_home.usecase.GetPromoUseCase
import com.mib.feature_home.usecase.GetSubcategoriesUseCase
import com.mib.feature_home.usecase.RegisterUseCase
import com.mib.feature_home.usecase.auth.SendCodeUseCase
import com.mib.feature_home.usecase.home.GetHomeContentUseCase
import com.mib.lib_auth.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HomeUseCaseModule {
    @Provides
    fun provideGetHomeContentUseCase(repo: HomeRepository): GetHomeContentUseCase {
        return GetHomeContentUseCase(repo)
    }

    @Provides
    fun provideGetCategoriesUseCase(repo: HomeRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repo)
    }

    @Provides
    fun provideGetSubcategoriesUseCase(repo: HomeRepository): GetSubcategoriesUseCase {
        return GetSubcategoriesUseCase(repo)
    }

    @Provides
    fun provideGetProductsUseCase(repo: HomeRepository): GetProductsUseCase {
        return GetProductsUseCase(repo)
    }

    @Provides
    fun provideGetPromoUseCase(repo: HomeRepository): GetPromoUseCase {
        return GetPromoUseCase(repo)
    }

    @Provides
    fun provideRegisterUseCase(
        repo: HomeRepository,
        sessionRepo: SessionRepository
    ): RegisterUseCase {
        return RegisterUseCase(repo, sessionRepo)
    }

    @Provides
    fun provideSendCodeUseCase(repo: HomeRepository): SendCodeUseCase {
        return SendCodeUseCase(repo)
    }

    @Provides
    fun provideGetProductDetailUseCase(repo: HomeWithAuthRepository): GetProductDetailUseCase {
        return GetProductDetailUseCase(repo)
    }
}