package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class BannerResponse (
    @SerializedName("key")
    val key: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("image_path")
    val imageUrl: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
)
