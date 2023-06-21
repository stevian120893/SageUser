package com.mib.feature_home.dto.response.order_detail

import com.google.gson.annotations.SerializedName

class PaymentMethodResponse (
    @SerializedName("key")
    val key: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("is_active")
    val isActive: Boolean?
)
