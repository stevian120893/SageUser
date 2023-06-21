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
    val orderDate: String?,
    @SerializedName("booking_date")
    val bookingDate: String?,
    @SerializedName("order_accepted_at")
    val orderAcceptedAt: String?,
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
    val paymentSuccessAt: String?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("detail")
    val detail: DetailResponse?,
    @SerializedName("payment_method")
    val paymentMethod: List<PaymentMethodResponse>?
) {
    companion object {
        @StringDef(
            ONGOING,
//            IN_PROGRESS,
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val ONGOING = "ONGOING"
//        const val IN_PROGRESS = "inprogress"
    }
}
