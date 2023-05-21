package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.OrderHistory
import com.mib.feature_home.dto.response.OrderHistoryResponse
import java.math.BigDecimal

fun OrderHistoryResponse.toDomainModel(): OrderHistory {
    return OrderHistory(
        merchantId = this.merchantId.orEmpty(),
        merchantName = this.merchantName.orEmpty(),
        code = this.code.orEmpty(),
        address = this.address.orEmpty(),
        status = this.status.orEmpty(),
        orderDate = this.orderDate.orEmpty(),
        bookingDate = this.bookingDate.orEmpty(),
        totalPayment = this.totalPayment ?: BigDecimal.ZERO,
        note = this.note.orEmpty(),
    )
}
