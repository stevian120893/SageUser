package com.mib.sage_user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mib.lib_auth.repository.SessionRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var sessionRepository: SessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Handle the splash screen transition (Android 12)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        setContentView(R.layout.activity_main)
        setupNavGraph()
        setFirebaseToken()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        if (intent != null) {
//            val deeplinkResult = deeplinkDelegate.dispatchFrom(this, intent)
//            intent.data?.let { adjustTracker.trackDeeplink(it, this) }
//            if (!deeplinkResult.isSuccessful) {
//                intent.data?.toString()?.let {
//                    browserNavigation.launchBrowser(this, it, false)
//                }
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // to check the status of in app update result. when the result is cancelled
        // and update type is force update, then should finish the activity
//        if (requestCode == AppUpdate.UPDATE_REQUEST_CODE) {
            val isCancelled = resultCode != Activity.RESULT_OK
//            val isForceUpdate = appUpdatePref.lastUpdateType == AppUpdate.TYPE_FORCE
//            if (isCancelled && isForceUpdate) {
//                finish()
//            }
//        }
    }

    private fun setupNavGraph() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment
        val navInflater = navHostFragment.navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.nav_graph)

//        val isLoggedIn = !sessionRepository.getAccessToken().isNullOrBlank()
//        val skipValueProposition = !homePref.shouldShowValueProposition
        navGraph.startDestination = R.id.homeFragment
//        if (isLoggedIn) {
//            R.id.homeFragment
//        } else {
//            R.id.loginFragment
//        }

        navHostFragment.navController.graph = navGraph
    }

    private fun setFirebaseToken() {
        val isLoggedIn = !sessionRepository.getAccessToken().isNullOrBlank()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            if(isLoggedIn) {
                // TODO check if the token is the same with the saved local one, if not then call save token API
            }
        })
    }


    public override fun onResume() {
        super.onResume()
    }
    public override fun onPause() {
        super.onPause()
    }
}
