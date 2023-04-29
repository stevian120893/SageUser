package com.mib.feature_home.dto.response

import com.google.gson.annotations.SerializedName

class PaymentMethodResponse (
    @SerializedName("is_accept_cash")
    val isAcceptCash: Boolean?,
    @SerializedName("is_accept_bank_transfer")
    val isAcceptBankTransfer: Boolean?
)
