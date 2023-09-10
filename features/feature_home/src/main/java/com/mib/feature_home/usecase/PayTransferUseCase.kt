package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class PayTransferUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        code: String,
        referenceId: String,
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.payTransfer(code, referenceId)
    }
}