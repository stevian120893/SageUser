package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.mapper.toDomainModel
import com.mib.feature_home.service.HomeAuthenticatedService
import com.mib.lib_api.dto.NetworkResponse

class HomeWithAuthRepositoryImpl(
    service: () -> HomeAuthenticatedService
): HomeWithAuthRepository {
    private val service by lazy(service)

    override suspend fun getProductDetail(productCode: String): Pair<ProductDetail?, String?> {
        return when (val result = service.getProductDetail(productCode)) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getProfile(): Pair<Profile?, String?> {
        return when (val result = service.getProfile()) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getPromos(
        cursor: String?
    ): Pair<PromoItemPaging, String?> {
        val result = service.getPromo(cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                PromoItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                PromoItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }
}