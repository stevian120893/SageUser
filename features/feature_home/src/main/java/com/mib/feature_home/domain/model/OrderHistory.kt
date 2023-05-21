package com.mib.feature_home.domain.model

import java.math.BigDecimal

class OrderHistory (
    var merchantId: String,
    var merchantName: String,
    var code: String,
    var address: String,
    var status: String,
    var orderDate: String,
    var bookingDate: String,
    var totalPayment: BigDecimal,
    var note: String
)