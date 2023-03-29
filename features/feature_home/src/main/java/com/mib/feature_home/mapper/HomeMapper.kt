package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Banner
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.dto.response.HomeResponse

fun HomeResponse.toDomainModel(): Home {
    val banners = banner?.map { content ->
        Banner(
            key = content.key.orEmpty(),
            type = content.type.orEmpty(),
            imageUrl = content.imageUrl.orEmpty(),
            title = content.title.orEmpty(),
            description = content.description.orEmpty()
        )
    }.orEmpty()

    val categories = category?.map { content ->
        Category(
            categoryId = content.categoryId.orEmpty(),
            categoryName = content.categoryName.orEmpty(),
            status = content.status.orEmpty()
        )
    }.orEmpty()

    return Home(
        banners = banners,
        categories = categories
    )
}
