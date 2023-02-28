package com.mib.feature_home.dto.request

import com.google.gson.annotations.SerializedName

class AvailabilityDayRequest(
//    @SerializedName("availability_day_code")
//    val availabilityDayCode: String,
    @SerializedName("day_name")
    val dayName: String,
    @SerializedName("open_time")
    val openHour: String,
    @SerializedName("close_time")
    val closedHour: String,
    @SerializedName("is_open")
    val isOpen: Boolean
)