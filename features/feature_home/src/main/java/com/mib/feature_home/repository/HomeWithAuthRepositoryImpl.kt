package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.domain.model.OrderHistoryItemPaging
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.dto.request.OrderRequest
import com.mib.feature_home.dto.request.PayDanaRequest
import com.mib.feature_home.dto.request.PayTransferRequest
import com.mib.feature_home.dto.request.SendRatingRequest
import com.mib.feature_home.dto.request.VerificationCodeRequest
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

    override suspend fun bookOrder(
        productCode: String,
        address: String,
        date: String,
        note: String,
    ): Pair<Order?, String?> {
        val orderRequest = OrderRequest(
            productCode = productCode,
            address = address,
            bookingDate = date,
            note = note
        )
        val result = service.bookOrder(orderRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getOrderHistory(
        cursor: String?
    ): Pair<OrderHistoryItemPaging, String?> {
        val result = service.getOrderHistory(cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                OrderHistoryItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                OrderHistoryItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
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

    override suspend fun getOrderDetail(orderId: String): Pair<OrderDetail?, String?> {
        return when (val result = service.getOrderDetail(orderId)) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun sendRating(
        orderId: String,
        rating: String,
        review: String
    ): Pair<Void?, String?> {
        val sendRatingRequest = SendRatingRequest(
            orderId = orderId,
            rating = rating,
            review = review
        )
        val result = service.sendRating(sendRatingRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun payDana(
        code: String
    ): Pair<Void?, String?> {
        val payDanaRequest = PayDanaRequest(
            code = code
        )
        val result = service.payDana(payDanaRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun payTransfer(
        code: String,
        referenceId: String,
    ): Pair<Void?, String?> {
        val payTransferRequest = PayTransferRequest(
            code = code,
            referenceId = referenceId
        )
        val result = service.payTransfer(payTransferRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }
}