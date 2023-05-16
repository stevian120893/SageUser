package com.mib.feature_home.domain.model

class Promo (
    val promoCode: String,
    val promoTitle: String,
    val description: String,
    val inputCode: String,
    val quota: String,
    val type: String,
    val discount: String,
    val minimumAmount: String,
    val maximumDiscount: String,
    val promoStartDate: Long,
    val promoExpiredDate: Long,
    val isTimeLimited: Boolean,
    val promoImageUrl: String,
    val status: String
)
