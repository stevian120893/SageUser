package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class PayDanaResponse (
    @SerializedName("payment_url")
    val paymentUrl: String
)
