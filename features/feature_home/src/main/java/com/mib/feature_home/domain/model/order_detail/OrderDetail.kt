package com.mib.feature_home.domain.model.order_detail

import androidx.annotation.StringDef
import java.math.BigDecimal

class OrderDetail (
    val merchant: Merchant,
    val code: String,
    val address: String,
    @Status val status: String,
    val orderDate: String,
    val bookingDate: String,
    val orderAcceptedAt: String,
    val totalPrice: BigDecimal,
    val discount: BigDecimal,
    val totalPayment: BigDecimal,
    val usedPaymentMethod: String,
    val paymentReceiptImage: String,
    val paymentSuccessAt: String,
    val note: String,
    val detail: Detail,
    val paymentMethod: List<PaymentMethod>
) {
    companion object {
        @StringDef(
            NEGOTIATING,
            WAITING_FOR_PAYMENT,
            ONGOING,
            CANCEL,
            DONE,
            UNKNOWN
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val NEGOTIATING = "NEGOTIATING"
        const val WAITING_FOR_PAYMENT = "WAITING_FOR_PAYMENT"
        const val ONGOING = "ONGOING"
        const val CANCEL = "CANCEL"
        const val DONE = "DONE"
        const val UNKNOWN = "UNKNOWN"
    }
}
