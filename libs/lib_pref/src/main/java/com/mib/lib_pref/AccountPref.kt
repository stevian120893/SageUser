package com.mib.lib_pref

import android.content.SharedPreferences
import com.mib.lib_pref.file.UserBoundPref

/**
 * Shared preference to save data related to user's account information
 */
class AccountPref(
    sharedPref: () -> SharedPreferences
) {

    private val _sharedPref by lazy(sharedPref)

    var fullname by _sharedPref.string(key = FULLNAME)

    var phoneNumber by _sharedPref.string(key = PHONE_NUMBER)

    var email by _sharedPref.string(key = EMAIL)

    var address by _sharedPref.string(key = ADDRESS)

    var location by _sharedPref.string(key = LOCATION)

    companion object {
        private const val FULLNAME = "fullname"
        private const val PHONE_NUMBER = "phoneNumber"
        private const val EMAIL = "email"
        private const val ADDRESS = "address"
        private const val LOCATION = "location"

        fun get(): AccountPref {
            return AccountPref(UserBoundPref::create)
        }
    }
}
