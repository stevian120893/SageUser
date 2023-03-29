package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.repository.HomeRepository

class GetCategoriesUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(cursor: String? = null): Pair<CategoriesItemPaging, String?> {
        return homeRepository.getCategories(cursor)
    }
}