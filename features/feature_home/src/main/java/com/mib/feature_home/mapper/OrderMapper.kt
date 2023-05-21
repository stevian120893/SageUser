package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.dto.response.OrderResponse

fun OrderResponse.toDomainModel(): Order {
    return Order(
        orderId = this.orderId
    )
}
