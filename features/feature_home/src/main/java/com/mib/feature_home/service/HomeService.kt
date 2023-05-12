package com.mib.feature_home.service

import com.mib.feature_home.dto.request.VerificationCodeRequest
import com.mib.feature_home.dto.response.CategoryResponse
import com.mib.feature_home.dto.response.HomeResponse
import com.mib.feature_home.dto.response.CityResponse
import com.mib.feature_home.dto.response.ProductResponse
import com.mib.feature_home.dto.response.PromoResponse
import com.mib.feature_home.dto.response.SubcategoryResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import com.mib.lib_auth.dto.response.TokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface HomeService {
    @GET("/site/home")
    suspend fun getHomeContent(): NetworkResponse<ApiResponse<HomeResponse>>

    @GET("/category/list")
    suspend fun getCategories(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<CategoryResponse>>>

    @GET("/subcategory/list")
    suspend fun getSubcategories(
        @Query("page") cursor: String?,
        @Query("category_code") categoryCode: String?,
    ): NetworkResponse<ApiResponse<List<SubcategoryResponse>>>

    @GET("/product/list")
    suspend fun getProducts(
        @Query("page") cursor: String?,
        @Query("category_code") categoryCode: String?,
        @Query("subcategory_code") subcategoryCode: String?,
        @Query("search") search: String?,
    ): NetworkResponse<ApiResponse<List<ProductResponse>>>

    @GET("/promo/list")
    suspend fun getPromo(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<PromoResponse>>>

    @Multipart
    @POST("/auth/register")
    suspend fun register(
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("code") code: RequestBody?,
        @Part profilePicture: MultipartBody.Part?,
    ): NetworkResponse<ApiResponse<TokenResponse>>

    @POST("/auth/send-code")
    suspend fun sendCode(
        @Body body: VerificationCodeRequest
    ): NetworkResponse<ApiResponse<Void>>

    @GET("/public/cities")
    suspend fun getLocations(): NetworkResponse<ApiResponse<List<CityResponse>>>
}
