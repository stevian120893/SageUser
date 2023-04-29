package com.mib.feature_home.service

import com.mib.feature_home.contents.product_detail.TempProductDetailRequest
import com.mib.feature_home.dto.response.ProductDetailResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeAuthenticatedService {
    @POST("/product/detail")
    suspend fun getProductDetail(
        @Body body: TempProductDetailRequest
    ): NetworkResponse<ApiResponse<ProductDetailResponse>>
}
