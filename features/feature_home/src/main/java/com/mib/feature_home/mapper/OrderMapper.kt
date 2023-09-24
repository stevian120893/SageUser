package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.BookOrder
import com.mib.feature_home.dto.response.OrderResponse

fun OrderResponse.toDomainModel(): BookOrder {
    return BookOrder(
        orderId = this.orderId
    )
}
