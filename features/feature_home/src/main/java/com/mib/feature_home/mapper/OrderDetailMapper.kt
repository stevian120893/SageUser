package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.order_detail.Detail
import com.mib.feature_home.domain.model.order_detail.Merchant
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.domain.model.order_detail.PaymentMethod
import com.mib.feature_home.domain.model.order_detail.Product
import com.mib.feature_home.dto.response.order_detail.OrderDetailResponse
import java.math.BigDecimal

fun OrderDetailResponse.toDomainModel(): OrderDetail {
    val paymentMethods = paymentMethod?.map { content ->
        PaymentMethod(
            key = content.key.orEmpty(),
            name = content.name.orEmpty(),
            isActive = content.isActive ?: false
        )
    }.orEmpty()

    val merchant = Merchant(
        name = this.merchant?.name.orEmpty(),
        email = this.merchant?.email.orEmpty(),
        bankCode = this.merchant?.bankCode.orEmpty(),
        bankName = this.merchant?.bankName.orEmpty(),
        bankAccountNumber = this.merchant?.bankAccountNumber.orEmpty()
    )

    val product = Product(
        code = this.detail?.product?.code.orEmpty(),
        name = this.detail?.product?.name.orEmpty()
    )

    val detail = Detail(
        product = product,
        price = this.detail?.price ?: BigDecimal.ZERO,
        qty = this.detail?.qty ?: 0,
        totalPrice = this.detail?.totalPrice ?: BigDecimal.ZERO
    )

    val status = when (status) {
        OrderDetailResponse.ONGOING -> OrderDetail.ONGOING
//        OrderDetailResponse.EXPIRED, WalletTransactionResponse.FAILED -> WalletTransactionItem.FAILED
        else -> OrderDetail.UNKNOWN
    }

    return OrderDetail(
        merchant = merchant,
        code = this.code.orEmpty(),
        address = this.address.orEmpty(),
        status = status,
        orderDate = this.orderDate.orEmpty(),
        bookingDate = this.bookingDate.orEmpty(),
        orderAcceptedAt = this.orderAcceptedAt.orEmpty(),
        totalPrice = this.totalPrice ?: BigDecimal.ZERO,
        discount = this.discount ?: BigDecimal.ZERO,
        totalPayment = this.totalPayment ?: BigDecimal.ZERO,
        usedPaymentMethod = this.usedPaymentMethod.orEmpty(),
        paymentReceiptImage = this.paymentReceiptImage.orEmpty(),
        paymentSuccessAt = this.paymentSuccessAt.orEmpty(),
        note = this.note.orEmpty(),
        detail = detail,
        paymentMethod = paymentMethods
    )
}
