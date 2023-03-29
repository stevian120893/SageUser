package com.mib.feature_home.usecase.home

import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.repository.HomeRepository

class GetHomeContentUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Pair<Home?, String?> {
        return homeRepository.getHomeContent()
    }
}
