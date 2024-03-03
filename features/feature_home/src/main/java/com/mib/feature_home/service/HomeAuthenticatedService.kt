package com.mib.feature_home.service

import com.mib.feature_home.dto.request.OrderRequest
import com.mib.feature_home.dto.request.PayDanaRequest
import com.mib.feature_home.dto.request.SendRatingRequest
import com.mib.feature_home.dto.request.SetFcmTokenRequest
import com.mib.feature_home.dto.response.OrderHistoryResponse
import com.mib.feature_home.dto.response.OrderResponse
import com.mib.feature_home.dto.response.PayDanaResponse
import com.mib.feature_home.dto.response.ProductDetailResponse
import com.mib.feature_home.dto.response.ProfileResponse
import com.mib.feature_home.dto.response.PromoResponse
import com.mib.feature_home.dto.response.order_detail.OrderDetailResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeAuthenticatedService {
    @GET("/product/detail")
    suspend fun getProductDetail(
        @Query("code") code: String
    ): NetworkResponse<ApiResponse<ProductDetailResponse>>

    @GET("/profile")
    suspend fun getProfile(
    ): NetworkResponse<ApiResponse<ProfileResponse>>

    @POST("/order/booking")
    suspend fun bookOrder(
        @Body body: OrderRequest
    ): NetworkResponse<ApiResponse<OrderResponse>>

    @GET("/history")
    suspend fun getOrderHistory(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<OrderHistoryResponse>>>

    @GET("/promo")
    suspend fun getPromo(
        @Query("page") cursor: String?
    ): NetworkResponse<ApiResponse<List<PromoResponse>>>

    @GET("/order/detail/{order_id}")
    suspend fun getOrderDetail(
        @Path("order_id") orderId: String
    ): NetworkResponse<ApiResponse<OrderDetailResponse>>

    @POST("/product/post-rating")
    suspend fun sendRating(
        @Body body: SendRatingRequest
    ): NetworkResponse<ApiResponse<Void>>

    @POST("/order/pay-with-dana")
    suspend fun payDana(
        @Body body: PayDanaRequest
    ): NetworkResponse<ApiResponse<PayDanaResponse>>

    @Multipart
    @POST("/order/pay-order")
    suspend fun payTransfer(
        @Part("code") code: RequestBody?,
        @Part paymentReceiptImage: MultipartBody.Part?
    ): NetworkResponse<ApiResponse<Void>>

    @POST("/site/set-fcm")
    suspend fun setFcmToken(
        @Body body: SetFcmTokenRequest
    ): NetworkResponse<ApiResponse<Void>>
}
