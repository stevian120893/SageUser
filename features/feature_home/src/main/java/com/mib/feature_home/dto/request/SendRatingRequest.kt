package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class SendRatingRequest(
    @SerializedName("code")
    val productId: String,
    @SerializedName("review")
    val review: String,
    @SerializedName("star")
    val rating: String
)
