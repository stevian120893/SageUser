package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.BookOrder
import com.mib.feature_home.domain.model.OrderHistoryItemPaging
import com.mib.feature_home.domain.model.PayDana
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.domain.model.PromoItemPaging
import com.mib.feature_home.domain.model.order_detail.OrderDetail
import okhttp3.MultipartBody

interface HomeWithAuthRepository {
    suspend fun getProductDetail(
        productCode: String
    ): Pair<ProductDetail?, String?>

    suspend fun getProfile(
    ): Pair<Profile?, String?>

    suspend fun bookOrder(
        productCode: String,
        address: String,
        date: String,
        note: String
    ): Pair<BookOrder?, String?>

    suspend fun getOrderHistory(
        cursor: String?
    ): Pair<OrderHistoryItemPaging?, String?>

    suspend fun getPromos(
        cursor: String?
    ): Pair<PromoItemPaging, String?>

    suspend fun getOrderDetail(
        orderId: String
    ): Pair<OrderDetail?, String?>

    suspend fun sendRating(
        productId: String,
        rating: String,
        review: String
    ): Pair<Void?, String?>

    suspend fun payDana(
        code: String
    ): Pair<PayDana?, String?>

    suspend fun payTransfer(
        code: String,
        referenceId: String? = null,
        paymentReceiptImage: MultipartBody.Part?
    ): Pair<Void?, String?>

    suspend fun saveFcmToken(
        fcmToken: String
    ): Pair<Void?, String?>
}