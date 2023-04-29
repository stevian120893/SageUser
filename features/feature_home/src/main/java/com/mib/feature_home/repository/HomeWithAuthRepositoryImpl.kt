package com.mib.feature_home.repository

import com.mib.feature_home.contents.product_detail.TempProductDetailRequest
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.dto.request.VerificationCodeRequest
import com.mib.feature_home.mapper.toDomainModel
import com.mib.feature_home.service.HomeAuthenticatedService
import com.mib.lib_api.dto.NetworkResponse

class HomeWithAuthRepositoryImpl(
    service: () -> HomeAuthenticatedService
): HomeWithAuthRepository {
    private val service by lazy(service)

    override suspend fun getProductDetail(productCode: String): Pair<ProductDetail?, String?> {
        val temp = TempProductDetailRequest(
            code = productCode
        )

        return when (val result = service.getProductDetail(temp)) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }
}