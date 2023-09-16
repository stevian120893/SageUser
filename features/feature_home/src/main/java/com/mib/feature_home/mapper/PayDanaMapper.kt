package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.PayDana
import com.mib.feature_home.dto.response.PayDanaResponse

fun PayDanaResponse.toDomainModel(): PayDana {
    return PayDana(
        paymentUrl = this.paymentUrl
    )
}
