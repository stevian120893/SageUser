package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.order_detail.Detail
import com.mib.feature_home.domain.model.order_detail.Merchant
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.ONGOING
import com.mib.feature_home.domain.model.order_detail.OrderDetail.Companion.UNKNOWN
import com.mib.feature_home.domain.model.order_detail.PaymentMethod
import com.mib.feature_home.domain.model.order_detail.Product
import com.mib.feature_home.dto.response.order_detail.OrderDetailResponse
import com.mib.feature_home.utils.AppUtils
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

    return OrderDetail(
        merchant = merchant,
        code = this.code.orEmpty(),
        address = this.address.orEmpty(),
        status = status.orEmpty(),
        orderDate = AppUtils.convertMillisToDate(this.orderDate),
        bookingDate = AppUtils.convertMillisToDate(this.bookingDate),
        orderAcceptedAt = AppUtils.convertMillisToDate(this.orderAcceptedAt),
        totalPrice = this.totalPrice ?: BigDecimal.ZERO,
        discount = this.discount ?: BigDecimal.ZERO,
        totalPayment = this.totalPayment ?: BigDecimal.ZERO,
        usedPaymentMethod = this.usedPaymentMethod.orEmpty(),
        paymentReceiptImage = this.paymentReceiptImage.orEmpty(),
        paymentSuccessAt = AppUtils.convertMillisToDate(this.paymentSuccessAt),
        note = this.note.orEmpty(),
        detail = detail,
        paymentMethod = paymentMethods
    )
}
