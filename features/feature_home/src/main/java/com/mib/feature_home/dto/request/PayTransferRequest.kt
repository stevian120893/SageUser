package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class PayTransferRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("reference_id")
    val referenceId: String,
)