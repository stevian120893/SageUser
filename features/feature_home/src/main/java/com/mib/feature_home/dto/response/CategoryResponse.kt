package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class CategoryResponse (
    @SerializedName("code")
    val categoryCode: String?,
    @SerializedName("name")
    val categoryName: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("image_path")
    val imageUrl: String?
)
