package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class OrderResponse (
    @SerializedName("order_id")
    val orderId: String
)
