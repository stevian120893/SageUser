package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.dto.response.PromoResponse

fun PromoResponse.toDomainModel(): Promo {
    return Promo(
        promoCode = this.promoCode.orEmpty(),
        promoTitle = this.promoTitle.orEmpty(),
        description = this.description.orEmpty(),
        type = this.type ?: 0,
        promoStartDate = this.promoStartDate ?: 0L,
        promoExpiredDate = this.promoExpiredDate ?: 0L,
        isTimeLimited = this.isTimeLimited ?: false,
        promoImageUrl = this.promoImageUrl.orEmpty(),
        status = this.status.orEmpty()
    )
}
