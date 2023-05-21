package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.repository.HomeWithAuthRepository

class BookOrderUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        productCode: String,
        address: String,
        date: String,
        note: String
    ): Pair<Order?, String?> {
        return homeWithAuthRepository.bookOrder(productCode, address, date, note)
    }
}