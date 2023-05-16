package com.mib.feature_home.service

import com.mib.feature_home.dto.response.ProductDetailResponse
import com.mib.feature_home.dto.response.ProfileResponse
import com.mib.feature_home.dto.response.PromoResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeAuthenticatedService {
    @GET("/product/detail")
    suspend fun getProductDetail(
        @Query("code") code: String
    ): NetworkResponse<ApiResponse<ProductDetailResponse>>

    @GET("/profile")
    suspend fun getProfile(
    ): NetworkResponse<ApiResponse<ProfileResponse>>

    @GET("/promo")
    suspend fun getPromo(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<PromoResponse>>>
}
