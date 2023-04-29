package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.ProductDetail

interface HomeWithAuthRepository {
    suspend fun getProductDetail(
        productCode: String
    ): Pair<ProductDetail?, String?>
}