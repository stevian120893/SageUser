package com.mib.feature_home.usecase

import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.repository.HomeRepository

class GetProductsUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(
        cursor: String? = null,
        categoryCode: String,
        subcategoryCode: String,
        searchKey: String? = null,
        cityCode: String? = null
    ): Pair<ProductsItemPaging, String?> {
        return homeRepository.getProducts(cursor, categoryCode, subcategoryCode, searchKey, cityCode)
    }
}