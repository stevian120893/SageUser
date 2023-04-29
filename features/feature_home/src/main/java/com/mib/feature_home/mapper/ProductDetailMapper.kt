package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.City
import com.mib.feature_home.domain.model.PaymentMethod
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.dto.response.ProductDetailResponse

fun ProductDetailResponse.toDomainModel(): ProductDetail {
    val location = City(
        code = this.location?.code.orEmpty(),
        name = this.location?.name.orEmpty()
    )

    val paymentMethod = PaymentMethod(
        isAcceptCash = this.paymentMethod?.isAcceptCash ?: false,
        isAcceptBankTransfer = this.paymentMethod?.isAcceptBankTransfer ?: false
    )

    return ProductDetail(
        imageUrl = this.imageUrl.orEmpty(),
        name = this.name.orEmpty(),
        description = this.description.orEmpty(),
        merchantName = this.merchantName.orEmpty(),
        rating = this.rating.orEmpty(),
        price = this.price.orEmpty(),
        operationTime = this.operationTime.orEmpty(),
        location = location,
        transactionUsage = this.transactionUsage ?: 0,
        serviceYearsExperience = this.serviceYearsExperience ?: 0,
        paymentMethod = paymentMethod,
    )
}