package com.mib.feature_home.utils

import android.content.Context
import com.mib.feature_home.R
import com.mib.feature_home.domain.model.OrderHistory.Companion.CANCEL
import com.mib.feature_home.domain.model.OrderHistory.Companion.DONE
import com.mib.feature_home.domain.model.OrderHistory.Companion.NEGOTIATING
import com.mib.feature_home.domain.model.OrderHistory.Companion.ONGOING
import com.mib.feature_home.domain.model.OrderHistory.Companion.WAITING_FOR_PAYMENT

object CustomUtils {
    fun getUserFriendlyOrderStatusName(
        context: Context,
        status: String
    ): String {
        return when (status) {
            NEGOTIATING -> context.getString(R.string.shared_res_status_negotiating)
            WAITING_FOR_PAYMENT -> context.getString(R.string.shared_res_status_waiting_for_payment)
            ONGOING -> context.getString(R.string.shared_res_status_ongoing)
            CANCEL -> context.getString(R.string.shared_res_status_cancel)
            DONE -> context.getString(R.string.shared_res_status_done)
            else -> context.getString(R.string.shared_res_status_unknown)
        }
    }
}