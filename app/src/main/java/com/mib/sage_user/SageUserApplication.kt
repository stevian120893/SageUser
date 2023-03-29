package com.mib.sage_user

import android.app.Application
import com.mib.lib_util.Utils
import com.mib.sage_user.config.SageAppConfig
import com.mib.sage_user.di.ApiConfigQualifier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SageUserApplication : Application() {

    @Inject @ApiConfigQualifier
    lateinit var apiAppConfig: SageAppConfig

    override fun onCreate() {
        super.onCreate()
        setupConfig()
    }

    private fun setupConfig() {
        Utils.init(this)
        apiAppConfig.init()
    }
}