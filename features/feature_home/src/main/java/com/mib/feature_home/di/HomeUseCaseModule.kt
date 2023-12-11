package com.mib.feature_home.di

import com.mib.feature_home.repository.HomeRepository
import com.mib.feature_home.repository.HomeWithAuthRepository
import com.mib.feature_home.usecase.BookOrderUseCase
import com.mib.feature_home.usecase.GetCategoriesUseCase
import com.mib.feature_home.usecase.GetOrderDetailUseCase
import com.mib.feature_home.usecase.GetOrderHistoryUseCase
import com.mib.feature_home.usecase.GetProductDetailUseCase
import com.mib.feature_home.usecase.GetProductsUseCase
import com.mib.feature_home.usecase.GetProfileUseCase
import com.mib.feature_home.usecase.GetPromoUseCase
import com.mib.feature_home.usecase.GetSubcategoriesUseCase
import com.mib.feature_home.usecase.PayDanaUseCase
import com.mib.feature_home.usecase.PayTransferUseCase
import com.mib.feature_home.usecase.RegisterUseCase
import com.mib.feature_home.usecase.SendRatingUseCase
import com.mib.feature_home.usecase.auth.SaveFcmTokenUseCase
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
    fun provideGetProfileUseCase(repo: HomeWithAuthRepository): GetProfileUseCase {
        return GetProfileUseCase(repo)
    }

    @Provides
    fun provideGetPromoUseCase(repo: HomeWithAuthRepository): GetPromoUseCase {
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
    fun provideBookOrderUseCase(
        repo: HomeWithAuthRepository,
    ): BookOrderUseCase {
        return BookOrderUseCase(repo)
    }

    @Provides
    fun provideGetOrderHistoryUseCase(repo: HomeWithAuthRepository): GetOrderHistoryUseCase {
        return GetOrderHistoryUseCase(repo)
    }

    @Provides
    fun provideSendCodeUseCase(repo: HomeRepository): SendCodeUseCase {
        return SendCodeUseCase(repo)
    }

    @Provides
    fun provideGetProductDetailUseCase(repo: HomeWithAuthRepository): GetProductDetailUseCase {
        return GetProductDetailUseCase(repo)
    }

    @Provides
    fun provideGetOrderDetailUseCase(repo: HomeWithAuthRepository): GetOrderDetailUseCase {
        return GetOrderDetailUseCase(repo)
    }

    @Provides
    fun provideSendRatingUseCase(repo: HomeWithAuthRepository): SendRatingUseCase {
        return SendRatingUseCase(repo)
    }

    @Provides
    fun providePayDanaUseCase(repo: HomeWithAuthRepository): PayDanaUseCase {
        return PayDanaUseCase(repo)
    }

    @Provides
    fun providePayTransferUseCase(repo: HomeWithAuthRepository): PayTransferUseCase {
        return PayTransferUseCase(repo)
    }

    @Provides
    fun provideSaveFcmTokenUseCase(repo: HomeRepository): SaveFcmTokenUseCase {
        return SaveFcmTokenUseCase(repo)
    }
}