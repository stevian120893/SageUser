package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class SendRatingUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        orderId: String,
        rating: String,
        review: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.sendRating(orderId, rating, review)
    }
}