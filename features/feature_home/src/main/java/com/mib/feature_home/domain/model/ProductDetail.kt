package com.mib.feature_home.domain.model

class ProductDetail (
    val imageUrl: String,
    val name: String,
    val description: String,
    val merchantName: String,
    val rating: String,
    val price: String,
    val operationTime: String,
    val location: City?,
    val transactionUsage: Int,
    val serviceYearsExperience: Int,
    val paymentMethod: PaymentMethod?
)