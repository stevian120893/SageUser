package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.repository.HomeRepository
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetPromoUseCase(private val homeRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        cursor: String? = null
    ): Pair<PromoItemPaging, String?> {
        return homeRepository.getPromos(cursor)
    }
}