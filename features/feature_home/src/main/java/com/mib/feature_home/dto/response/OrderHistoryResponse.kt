package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class OrderHistoryResponse (
    @SerializedName("merchantid")
    val merchantId: String?,
    @SerializedName("merchantname")
    val merchantName: String?,
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
    @SerializedName("total_payment")
    val totalPayment: BigDecimal?,
    @SerializedName("note")
    val note: String?
)
