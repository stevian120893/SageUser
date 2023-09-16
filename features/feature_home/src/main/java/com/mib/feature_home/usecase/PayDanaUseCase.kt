package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.PayDana
import com.mib.feature_home.repository.HomeWithAuthRepository

class PayDanaUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        code: String
    ): Pair<PayDana?, String?> {
        return homeWithAuthRepository.payDana(code)
    }
}