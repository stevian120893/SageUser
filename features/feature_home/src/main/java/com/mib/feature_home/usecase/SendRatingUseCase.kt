package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository

class SendRatingUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        productId: String,
        rating: String,
        review: String
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.sendRating(productId, rating, review)
    }
}