package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class OrderRequest(
    @SerializedName("product_code")
    val productCode: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("note")
    val note: String
)
