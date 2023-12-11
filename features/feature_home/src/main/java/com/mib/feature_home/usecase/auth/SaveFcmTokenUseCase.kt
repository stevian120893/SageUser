package com.mib.feature_home.usecase.auth

import com.mib.feature_home.repository.HomeRepository

class SaveFcmTokenUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(
        fcmToken: String
    ): Pair<Void?, String?> {
        return homeRepository.saveFcmToken(fcmToken)
    }
}