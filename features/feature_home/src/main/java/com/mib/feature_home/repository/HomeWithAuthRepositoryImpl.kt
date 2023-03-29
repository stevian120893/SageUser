package com.mib.feature_home.repository

import com.mib.feature_home.dto.request.AddCategoryRequest
import com.mib.feature_home.dto.request.AddSubcategoryRequest
import com.mib.feature_home.dto.request.AvailabilityDayRequest
import com.mib.feature_home.mapper.toDomainModel
import com.mib.feature_home.service.HomeAuthenticatedService
import com.mib.feature_home.utils.AppUtils
import com.mib.lib_api.dto.NetworkResponse
import okhttp3.MultipartBody

class HomeWithAuthRepositoryImpl(
    service: () -> HomeAuthenticatedService
): HomeWithAuthRepository {
    private val service by lazy(service)

//    override suspend fun getProfile(): Pair<Profile?, String?> {
//        val result = service.getProfile()
//        return when (result) {
//            is NetworkResponse.Success -> {
//                val item = result.value.data?.toDomainModel()
//                item to null
//            }
//            else -> {
//                null to result.getErrorMessage()
//            }
//        }
//    }
//
//    override suspend fun saveProfile(
//        name: String,
//        location: String,
//        bankCode: String,
//        bankAccountNumber: String,
//        isAcceptCash: Boolean,
//        isAcceptBankTransfer: Boolean,
//        profilePicture: MultipartBody.Part?
//    ): Pair<Profile?, String?> {
//        val result = service.saveProfile(
//            AppUtils.createRequestBody(name),
//            AppUtils.createRequestBody(location),
//            AppUtils.createRequestBody(bankCode),
//            AppUtils.createRequestBody(bankAccountNumber),
//            AppUtils.createRequestBody(if(isAcceptCash) "1" else "0"),
//            AppUtils.createRequestBody(if(isAcceptBankTransfer) "1" else "0"),
//            profilePicture
//        )
//        return when (result) {
//            is NetworkResponse.Success -> {
//                val item = result.value.data?.toDomainModel()
//                item to null
//            }
//            else -> {
//                null to result.getErrorMessage()
//            }
//        }
//    }
}