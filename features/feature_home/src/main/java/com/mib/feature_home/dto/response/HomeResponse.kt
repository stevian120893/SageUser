package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class HomeResponse (
    @SerializedName("banner")
    val banner: List<BannerResponse>?,
    @SerializedName("category")
    val category: List<CategoryResponse>?,
)
