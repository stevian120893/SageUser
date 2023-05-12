package com.mib.feature_home.domain.model

class Promo (
    val promoCode: String,
    val promoTitle: String,
    val description: String,
    val type: Int,
    val promoStartDate: Long,
    val promoExpiredDate: Long,
    val isTimeLimited: Boolean,
    val promoImageUrl: String,
    val status: String
)
