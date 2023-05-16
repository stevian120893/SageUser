package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Promo
import com.mib.feature_home.dto.response.PromoResponse

fun PromoResponse.toDomainModel(): Promo {
    return Promo(
        promoCode = this.promoCode.orEmpty(),
        promoTitle = this.promoTitle.orEmpty(),
        description = this.description.orEmpty(),
        inputCode = this.inputCode.orEmpty(),
        quota = this.quota.orEmpty(),
        type = this.type.orEmpty(),
        discount = this.discount.orEmpty(),
        minimumAmount = this.minimumAmount.orEmpty(),
        maximumDiscount = this.maximumDiscount.orEmpty(),
        promoStartDate = this.promoStartDate ?: 0L,
        promoExpiredDate = this.promoExpiredDate ?: 0L,
        isTimeLimited = this.isTimeLimited ?: false,
        promoImageUrl = this.promoImageUrl.orEmpty(),
        status = this.status.orEmpty()
    )
}
