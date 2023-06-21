package com.mib.feature_home.domain.model.order_detail

import java.math.BigDecimal

class Detail (
    val product: Product,
    val price: BigDecimal,
    val qty: Int,
    val totalPrice: BigDecimal,
)