package com.mib.feature_home.dto.response.order_detail

import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class OrderDetailResponse (
    @SerializedName("merchant")
    val merchant: MerchantResponse?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("order_date")
    val orderDate: Long?,
    @SerializedName("booking_date")
    val bookingDate: Long?,
    @SerializedName("order_accepted_at")
    val orderAcceptedAt: Long?,
    @SerializedName("total_price")
    val totalPrice: BigDecimal?,
    @SerializedName("discount")
    val discount: BigDecimal?,
    @SerializedName("total_payment")
    val totalPayment: BigDecimal?,
    @SerializedName("used_payment_method")
    val usedPaymentMethod: String?,
    @SerializedName("payment_receipt_image")
    val paymentReceiptImage: String?,
    @SerializedName("payment_success_at")
    val paymentSuccessAt: Long?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("detail")
    val detail: DetailResponse?,
    @SerializedName("payment_method")
    val paymentMethod: List<PaymentMethodResponse>?
) {
    companion object {
        @StringDef(
            NEGOTIATING,
            WAITING_FOR_PAYMENT,
            ONGOING,
            DONE,
            CANCEL,
            DELETED,
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val NEGOTIATING = "NEGOTIATING"
        const val WAITING_FOR_PAYMENT = "WAITING_FOR_PAYMENT"
        const val ONGOING = "ONGOING"
        const val DONE = "DONE"
        const val CANCEL = "CANCEL"
        const val DELETED = "DELETED"
    }
}
