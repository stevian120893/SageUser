package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeRepository
import com.mib.lib_auth.dto.response.TokenResponse
import com.mib.lib_auth.repository.SessionRepository
import okhttp3.MultipartBody

class RegisterUseCase(
    private val homeRepository: HomeRepository,
    private val sessionRepo: SessionRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        profilePicture: MultipartBody.Part?,
        code: String
    ): Pair<TokenResponse?, String?> {
        val response = homeRepository.register(
            email,
            password,
            name,
            phone,
            gender,
            profilePicture,
            code
        )
        // save the token in the session
        response.first?.let {
            sessionRepo.saveLoginInfo(it)
        }
        return response
    }
}