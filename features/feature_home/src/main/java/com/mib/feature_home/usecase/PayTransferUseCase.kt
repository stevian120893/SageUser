package com.mib.feature_home.usecase

import com.mib.feature_home.repository.HomeWithAuthRepository
import okhttp3.MultipartBody

class PayTransferUseCase(private val homeWithAuthRepository: HomeWithAuthRepository) {
    suspend operator fun invoke(
        code: String,
        referenceId: String? = null,
        paymentReceiptImage: MultipartBody.Part?
    ): Pair<Void?, String?> {
        return homeWithAuthRepository.payTransfer(code, referenceId, paymentReceiptImage)
    }
}