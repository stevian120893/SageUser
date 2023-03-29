package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.repository.HomeRepository

class GetSubcategoriesUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(cursor: String? = null, categoryCode: String? = null): Pair<SubcategoriesItemPaging, String?> {
        return homeRepository.getSubcategories(cursor, categoryCode)
    }
}