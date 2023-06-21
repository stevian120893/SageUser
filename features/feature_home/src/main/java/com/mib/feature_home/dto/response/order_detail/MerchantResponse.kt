package com.mib.feature_home.dto.response.order_detail

import com.google.gson.annotations.SerializedName

class MerchantResponse (
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("bank_code")
    val bankCode: String?,
    @SerializedName("bank_name")
    val bankName: String?,
    @SerializedName("bank_account_number")
    val bankAccountNumber: String?
)
