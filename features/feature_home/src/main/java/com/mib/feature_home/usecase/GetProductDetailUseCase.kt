package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetProductDetailUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        productCode: String
    ): Pair<ProductDetail?, String?> {
        return homeWithAuthRepository.getProductDetail(productCode)
    }
}