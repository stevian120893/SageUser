package com.mib.lib_user.model

import java.io.Serializable

class UserInfo(
    val id: Long,
    val fullName: String,
    val email: String?,
    val phone: String,
    val address: String?,
): Serializable
