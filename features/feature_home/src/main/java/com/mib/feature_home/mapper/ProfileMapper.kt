package com.mib.feature_home.mapper

import com.mib.feature_home.domain.model.Profile
import com.mib.feature_home.dto.response.ProfileResponse

fun ProfileResponse.toDomainModel(): Profile {
    return Profile(
        name = this.name.orEmpty(),
        phone = this.phone.orEmpty(),
        email = this.email.orEmpty(),
        profilePictureImageUrl = this.profilePictureImageUrl.orEmpty()
    )
}
