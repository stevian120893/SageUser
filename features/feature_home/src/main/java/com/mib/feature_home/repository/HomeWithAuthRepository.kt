package com.mib.feature_home.repository

import com.mib.feature_home.domain.model.Order
import com.mib.feature_home.domain.model.OrderHistoryItemPaging
import com.mib.feature_home.domain.model.ProductDetail
import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.domain.model.PromoItemPaging

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
    ): Pair<Order?, String?>

    suspend fun getOrderHistory(
        cursor: String?
    ): Pair<OrderHistoryItemPaging?, String?>

    suspend fun getPromos(
        cursor: String?
    ): Pair<PromoItemPaging, String?>
}