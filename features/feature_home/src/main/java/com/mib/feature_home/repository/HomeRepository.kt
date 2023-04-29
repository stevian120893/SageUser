package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody

interface HomeRepository {
    suspend fun getHomeContent(): Pair<Home?, String?>
    suspend fun getCategories(cursor: String?): Pair<CategoriesItemPaging, String?>
    suspend fun getSubcategories(cursor: String?, categoryCode: String?): Pair<SubcategoriesItemPaging, String?>
    suspend fun getProducts(
        cursor: String?,
        categoryCode: String?,
        subcategoryCode: String?,
        searchKey: String?
    ): Pair<ProductsItemPaging, String?>
    suspend fun register(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        profilePicture: MultipartBody.Part?,
        code: String
    ): Pair<TokenResponse?, String?>
    suspend fun sendCode(
        email: String
    ): Pair<Void?, String?>
}