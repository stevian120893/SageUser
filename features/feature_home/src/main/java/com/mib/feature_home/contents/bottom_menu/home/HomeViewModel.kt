package com.mib.feature_home.contents.bottom_menu.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mib.feature_home.contents.bottom_menu.home.HomeFragment.Companion.CATEGORY_ALL
import com.mib.feature_home.domain.model.Banner
import com.mib.feature_home.domain.model.Category
import com.mib.feature_home.domain.model.City
import com.mib.feature_home.domain.model.Location
import com.mib.feature_home.interfaces.ListenerCityList
import com.mib.feature_home.interfaces.ListenerTwoActions
import com.mib.feature_home.usecase.auth.SaveFcmTokenUseCase
import com.mib.feature_home.usecase.home.GetHomeContentUseCase
import com.mib.feature_home.utils.AppUtils
import com.mib.feature_home.utils.DialogUtils
import com.mib.feature_home.utils.Gps
import com.mib.lib.mvvm.BaseViewModel
import com.mib.lib.mvvm.BaseViewState
import com.mib.lib_auth.repository.SessionRepository
import com.mib.lib_coroutines.IODispatcher
import com.mib.lib_coroutines.MainDispatcher
import com.mib.lib_navigation.HomeNavigation
import com.mib.lib_navigation.LoadingDialogNavigation
import com.mib.lib_pref.AccountPref
import com.mib.lib_util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineContext,
    @MainDispatcher private val mainDispatcher: CoroutineContext,
    private val homeNavigation: HomeNavigation,
    private val getHomeContentUseCase: GetHomeContentUseCase,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase,
    val loadingDialog: LoadingDialogNavigation,
    private val accountPref: AccountPref,
    private val sessionRepository: SessionRepository
) : BaseViewModel<HomeViewModel.ViewState>(ViewState()) {

    override val toastEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private var isGPS = false
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    fun getHomeContent() {
        state = state.copy(isLoadHome = true, event = EVENT_UPDATE_FIRST_ITEMS)
        viewModelScope.launch(ioDispatcher) {
            val result = getHomeContentUseCase()

            loadingDialog.dismiss()
            withContext(mainDispatcher) {
                result.first?.let {
                    state = state.copy(
                        banner = it.banners,
                        category = it.categories,
                        cities = it.cities.orEmpty(),
                        event = EVENT_UPDATE_FIRST_ITEMS,
                        isLoadHome = false
                    )
                }
                result.second?.let {
                    state = state.copy(isLoadHome = false)
                    toastEvent.postValue(it)
                }
            }
        }
    }

    fun goToCategoryListScreen(navController: NavController, categoryCode: String? = null) {
        val category = if(categoryCode != CATEGORY_ALL) categoryCode else null
        homeNavigation.goToCategoryListScreen(
            navController = navController,
            categoryCode = category,
            cityCode = state.cityChosen?.code
        )
    }

    fun goToSubcategoryListScreen(navController: NavController, categoryCode: String, categoryName: String) {
        homeNavigation.goToSubcategoryListScreen(navController, categoryCode, categoryName)
    }

    fun goToProductListScreen(
        navController: NavController,
        categoryCode: String? = null,
        subcategoryCode: String? = null,
        subcategoryName: String? = null,
        isSearch: Boolean = false
    ) {
        homeNavigation.goToProductListScreen(
            navController,
            categoryCode,
            subcategoryCode,
            subcategoryName,
            isSearch,
            state.cityChosen?.code
        )
    }

    fun showChooseLocationDialog(context: Context) {
        DialogUtils.showDialogList(context, state.cities, object : ListenerCityList {
            override fun action(city: City) {
                updateLocation(completeAddress = city.name)
            }
        })
    }

    fun chooseLocation(city: City) {
        state = state.copy(cityChosen = city, event = NO_EVENT)
        accountPref.location = state.cityChosen?.name.orEmpty()
    }

    fun initGps(context: Context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 10 * 1000.toLong() // 10 seconds
        locationRequest!!.fastestInterval = 5 * 1000.toLong() // 5 seconds

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        val completeAddress = getCompleteAddress(context, location.latitude, location.longitude)
                        updateLocation(location.latitude, location.longitude, completeAddress)

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient!!.removeLocationUpdates(locationCallback!!)
                        }
                    }
                }
            }
        }

        isGpsOn(context)
    }

    fun actionWhenGpsTurnedOn(context: Context) {
        isGPS = true
        getLocation(context)
    }

    fun hasLocation() = accountPref.location

    fun updateLocation(latitude: Double? = null, longitude: Double? = null, completeAddress: String?) {
        state = if(latitude != null && longitude != null) {
            state.copy(
                location = Location(
                    latitude = latitude,
                    longitude = longitude
                ),
                completeAddress = completeAddress,
                event = EVENT_UPDATE_LOCATION
            )
        } else {
            state.copy(
                completeAddress = completeAddress,
                event = EVENT_UPDATE_LOCATION
            )
        }
    }

    fun setFirebaseToken() {
        val isLoggedIn = !sessionRepository.getAccessToken().isNullOrBlank()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            if(isLoggedIn) {
                // TODO check if the token is the same with the saved local one, if not then call save token API
                if(sessionRepository.getFcmToken() != token)
                    saveFcmToken(token)
            }
        })
    }

    private fun saveFcmToken(token: String) {
        viewModelScope.launch(ioDispatcher) {
            val result = saveFcmTokenUseCase(token)

            withContext(mainDispatcher) {
                result.first?.let {
                    sessionRepository.saveFcmToken(token)
                }
                result.second?.let {
                    // failed to save fcm token to server
                    toastEvent.postValue(it)
                }
            }
        }
    }

    private fun getCompleteAddress(context: Context, latitude: Double, longitude: Double): String {
        return try {
            AppUtils.getCompleteAddress(context, latitude, longitude)
        } catch (e: Exception) {
            ""
        }
    }

    private fun isGpsOn(context: Context) {
        Dexter.withContext(context).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            @SuppressLint("MissingPermission")
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    if(AppUtils.isGPSEnabled(context)) {
                        isGPS = true
                        getLocation(context)
                    } else {
                        // Ask to turn on gps
                        Gps(context).turnGPSOn(context as Activity, object : Gps.onGpsListener {
                            override fun gpsStatus(isGPSEnable: Boolean) {
                                // turn on GPS
                                isGPS = isGPSEnable

                                if(isGPS) {
                                    getLocation(context)
                                } else {
                                    state = state.copy(location = null, completeAddress = null, event = EVENT_UPDATE_LOCATION)
                                }
                            }
                        })
                    }
                } else {
                    if(report.isAnyPermissionPermanentlyDenied) {
                        DialogUtils.showBottomDialogActivateLocationThroughSettings(context, object: ListenerTwoActions {
                            override fun firstAction() {
                                state = state.copy(location = null, completeAddress = null, event = EVENT_UPDATE_LOCATION)
                            }

                            override fun secondAction() {
                                AppUtils.goToAppSettings(context)
                            }
                        })
                    }
                    state = state.copy(location = null, completeAddress = null, event = EVENT_UPDATE_LOCATION)
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                token.continuePermissionRequest()
            }
        }).check()
    }

    private fun getLocation(context: Context) {
        Dexter.withContext(context).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            @SuppressLint("MissingPermission")
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    if(isGPS) {
                        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                val completeAddress = getCompleteAddress(context, location.latitude, location.longitude)
                                updateLocation(location.latitude, location.longitude, completeAddress)
                            } else {
                                mFusedLocationClient!!.requestLocationUpdates(locationRequest!!, locationCallback!!, null)
                            }
                        }
                    }
                } else {
                    state = state.copy(location = null, completeAddress = null, event = EVENT_UPDATE_LOCATION)
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                token.continuePermissionRequest()
            }
        }).check()
    }

    data class ViewState(
        var event: Int = NO_EVENT,
        var banner: List<Banner>? = null,
        var category: List<Category>? = null,
        var location: Location? = null,
        var completeAddress: String? = null,
        var cities: List<City>? = null,
        var cityChosen: City? = null,
        var isLoadHome: Boolean = false
    ) : BaseViewState

    companion object {
        const val NO_EVENT = 0
        const val EVENT_UPDATE_FIRST_ITEMS = 1
        const val EVENT_UPDATE_LOCATION = 2
    }
}