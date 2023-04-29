package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.dto.response.CategoryResponse

fun CategoryResponse.toDomainModel(): Category {
    return Category(
        categoryCode = this.categoryCode.orEmpty(),
        categoryName = this.categoryName.orEmpty(),
        status = this.status.orEmpty(),
        imageUrl = this.imageUrl.orEmpty()
    )
}
