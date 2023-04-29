package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class ProductDetailResponse (
    @SerializedName("image_path")
    val imageUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("merchant_name")
    val merchantName: String?,
    @SerializedName("rating")
    val rating: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("operation_time")
    val operationTime: String?,
    @SerializedName("location")
    val location: CityResponse?,
    @SerializedName("transaction_usage")
    val transactionUsage: Int?,
    @SerializedName("service_years_experience")
    val serviceYearsExperience: Int?,
    @SerializedName("payment_method")
    val paymentMethod: PaymentMethodResponse?
)
