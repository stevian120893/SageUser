package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.CategoriesItemPaging
import com.mib.feature_home.domain.model.Home
import com.mib.feature_home.domain.model.ProductsItemPaging
import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.domain.model.SubcategoriesItemPaging
import com.mib.feature_home.dto.request.SetFcmTokenRequest
import com.mib.feature_home.dto.request.VerificationCodeRequest
import com.mib.feature_home.mapper.toDomainModel
import com.mib.feature_home.service.HomeService
import com.mib.feature_home.utils.AppUtils
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody

class HomeRepositoryImpl(
    service: () -> HomeService
): HomeRepository {
    private val service by lazy(service)

    override suspend fun getHomeContent(): Pair<Home?, String?> {
        val result = service.getHomeContent()
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data?.toDomainModel()
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun getCategories(cursor: String?): Pair<CategoriesItemPaging, String?> {
        val result = service.getCategories(cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                CategoriesItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                CategoriesItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun getSubcategories(cursor: String?, categoryCode: String?): Pair<SubcategoriesItemPaging, String?> {
        val result = service.getSubcategories(cursor, categoryCode)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                SubcategoriesItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                SubcategoriesItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun getProducts(
        cursor: String?,
        categoryCode: String?,
        subcategoryCode: String?,
        searchKey: String?,
        cityCode: String?
    ): Pair<ProductsItemPaging, String?> {
        val result = service.getProducts(cursor, categoryCode, subcategoryCode, searchKey, cityCode)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                ProductsItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                ProductsItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun getPromos(
        cursor: String?
    ): Pair<PromoItemPaging, String?> {
        val result = service.getPromo(cursor)
        return when (result) {
            is NetworkResponse.Success -> {
                val items = result.value.data?.map { it.toDomainModel() } ?: emptyList()
                val nextCursor = result.value.meta?.nextCursor
                PromoItemPaging(
                    items,
                    nextCursor
                ) to null
            }
            else -> {
                PromoItemPaging(
                    emptyList(),
                    null
                ) to result.getErrorMessage()
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        profilePicture: MultipartBody.Part?,
        code: String
    ): Pair<TokenResponse?, String?> {
        val result = service.register(
            email = AppUtils.createRequestBody(email),
            password = AppUtils.createRequestBody(password),
            name = AppUtils.createRequestBody(name),
            phone = AppUtils.createRequestBody(phone),
            gender = AppUtils.createRequestBody(gender),
            code = AppUtils.createRequestBody(code),
            profilePicture = profilePicture,
        )
        return when (result) {
            is NetworkResponse.Success -> result.value.data to null
            else -> null to result.getErrorMessage()
        }
    }

    override suspend fun sendCode(email: String): Pair<Void?, String?> {
        val verificationCodeRequest = VerificationCodeRequest(
            email = email
        )
        val result = service.sendCode(verificationCodeRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }

    override suspend fun saveFcmToken(fcmToken: String): Pair<Void?, String?> {
        val setFcmTokenRequest = SetFcmTokenRequest(
            fcmToken = fcmToken
        )
        val result = service.setFcmToken(setFcmTokenRequest)
        return when (result) {
            is NetworkResponse.Success -> {
                val item = result.value.data
                item to null
            }
            else -> {
                null to result.getErrorMessage()
            }
        }
    }
}