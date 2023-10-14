package com.mib.feature_home.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.mib.feature_home.interfaces.ListenerTwoActions
import com.mib.feature_home.contents.bottom_menu.home.HomeFragment.Companion.GPS_REQUEST_CODE

class Gps(private val context: Context) {
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationRequest: LocationRequest = LocationRequest.create()

    fun turnGPSOn(activity: Activity, onGpsListener: onGpsListener?) {
        DialogUtils.showDialogGpsOff(context, object : ListenerTwoActions {
            override fun firstAction() {
                mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener((context as Activity)) { //  GPS is already enable, callback GPS status through listener
                        onGpsListener?.gpsStatus(true)
                    }
                    .addOnFailureListener(context) { e ->
                        when ((e as ApiException).statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                activity.startIntentSenderForResult(rae.resolution.intentSender, GPS_REQUEST_CODE, null, 0, 0, 0, null)
                            } catch (sie: SendIntentException) {
                                Log.i(ContentValues.TAG, "PendingIntent unable to execute request.")
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings."
                                Log.e(ContentValues.TAG, errorMessage)
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            }

            override fun secondAction() {
                onGpsListener?.gpsStatus(false)
            }
        })
    }

    interface onGpsListener {
        fun gpsStatus(isGPSEnable: Boolean)
    }

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
    }
}