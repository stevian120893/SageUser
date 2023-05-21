package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.OrderHistoryItemPaging
import com.mib.feature_home.repository.HomeWithAuthRepository

class GetOrderHistoryUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(cursor: String? = null): Pair<OrderHistoryItemPaging?, String?> {
        return homeWithAuthRepository.getOrderHistory(cursor)
    }
}