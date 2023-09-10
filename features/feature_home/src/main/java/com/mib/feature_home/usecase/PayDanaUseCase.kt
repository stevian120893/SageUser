package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class PayDanaUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        code: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.payDana(code)
    }
}