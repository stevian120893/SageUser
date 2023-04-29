package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class CityResponse (
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?
)
