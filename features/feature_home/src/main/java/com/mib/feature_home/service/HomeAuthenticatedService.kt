package com.mib.feature_home.service

import com.mib.feature_home.dto.response.ProfileResponse
import com.mib.lib_api.dto.ApiResponse
import com.mib.lib_api.dto.NetworkResponse
import retrofit2.http.GET

interface HomeAuthenticatedService {
    @GET("/profile")
    suspend fun getProfile(
    ): NetworkResponse<ApiResponse<ProfileResponse>>
}
