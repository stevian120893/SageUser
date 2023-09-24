package com.mib.feature_home.domain.model

import androidx.annotation.StringDef
import java.math.BigDecimal

class OrderHistory (
    var merchantId: String,
    var merchantName: String,
    var code: String,
    var address: String,
    @Status var status: String,
    var orderDate: String,
    var bookingDate: String,
    var totalPayment: BigDecimal,
    var note: String
) {
    companion object {
        @StringDef(
            NEGOTIATING,
            WAITING_FOR_PAYMENT,
            ONGOING,
            CANCEL,
            DONE
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status

        const val NEGOTIATING = "NEGOTIATING"
        const val WAITING_FOR_PAYMENT = "WAITING_FOR_PAYMENT"
        const val ONGOING = "ONGOING"
        const val CANCEL = "CANCEL"
        const val DONE = "DONE"
    }
}
