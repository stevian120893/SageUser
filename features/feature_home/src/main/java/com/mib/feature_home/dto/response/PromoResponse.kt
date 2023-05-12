package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class PromoResponse (
    @SerializedName("code")
    val promoCode: String?,
    @SerializedName("name")
    val promoTitle: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("type")
    val type: Int?,
    @SerializedName("start_date")
    val promoStartDate: Long?,
    @SerializedName("end_date")
    val promoExpiredDate: Long?,
    @SerializedName("is_time_limited")
    val isTimeLimited: Boolean?,
    @SerializedName("image_path")
    val promoImageUrl: String?,
    @SerializedName("status")
    val status: String?
)
