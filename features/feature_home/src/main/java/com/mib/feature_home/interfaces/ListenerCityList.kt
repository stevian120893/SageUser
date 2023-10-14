package com.mib.feature_home.interfaces

import com.mib.feature_home.domain.model.City

interface ListenerCityList {
    fun action(city: City)
}
