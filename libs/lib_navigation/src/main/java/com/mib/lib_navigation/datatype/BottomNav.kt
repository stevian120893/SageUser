package com.mib.lib_navigation.datatype

import androidx.annotation.IntDef

object BottomNav {
    const val TAB_UNDEFINED = -1
    const val TAB_HOME = 0
    const val TAB_PROMO = 1
    const val TAB_ORDER_HISTORY = 2
    const val TAB_PROFILE = 3

    @IntDef(
        TAB_UNDEFINED,
        TAB_HOME,
        TAB_PROMO,
        TAB_ORDER_HISTORY,
        TAB_PROFILE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BottomNavType
}
