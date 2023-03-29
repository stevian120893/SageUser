package com.mib.feature_home.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mib.feature_home.R
import com.mib.feature_home.contents.bottom_menu.home.HomeFragment
import com.mib.feature_home.contents.bottom_menu.profile.ProfileFragment
import com.mib.feature_home.contents.bottom_menu.promo.PromoFragment
import com.mib.lib.mvvm.BaseFragment
import com.mib.lib_navigation.datatype.BottomNav
import com.mib.lib_navigation.datatype.BottomNav.TAB_HOME
import com.mib.lib_navigation.datatype.BottomNav.TAB_ORDER_HISTORY
import com.mib.lib_navigation.datatype.BottomNav.TAB_PROFILE
import com.mib.lib_navigation.datatype.BottomNav.TAB_PROMO
import com.mib.lib_pref.SessionPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeTabFragment : BaseFragment<HomeTabViewModel>(0),
    DialogInterface.OnDismissListener {

    @Inject
    lateinit var sessionPref: SessionPref

    private var bottomNavigationView: BottomNavigationView? = null
    private val handler = Handler(Looper.getMainLooper())

    private var homeFragment = HomeFragment()
    private var promoFragment = PromoFragment()
    private var orderHistoryFragment = PromoFragment()
    private var profileFragment = ProfileFragment()
    private var activeFragment: Fragment? = null

    private val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> TAB_HOME
            R.id.navigation_promo -> TAB_PROMO
            R.id.navigation_order_history -> TAB_ORDER_HISTORY
            R.id.navigation_profile-> TAB_PROFILE
            else -> null
        }?.let {
            loadFragment(it, false)
        }
        true
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when {
                viewModel.backStack.isFirstPage() -> {
                    if (viewModel.backPressedOnce) {
                        activity?.finish()
                        return
                    }

                    viewModel.backPressedOnce = true
                    Toast.makeText(
                        requireActivity(),
                        R.string.home_text_back_button_exit,
                        Toast.LENGTH_SHORT
                    ).show()
                    handler.postDelayed({
                        viewModel.backPressedOnce = false
                    }, 2000)
                }
                else -> {
                    viewModel.backStack.goBack {
                        bottomNavigationView?.selectedItemId = getSelectedMenu(viewModel.backStack.currentPage)
                    }
                }
            }
        }
    }

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(HomeTabViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)

        if (savedInstanceState == null) {
            loadFragment(TAB_HOME, true)
        }

        // listen result from rationale dialog that comes from HomeFragment
        // because the fragment registered in navController is HomeTabFragment
//        parentFragmentManager.setFragmentResultListener(
//            RationaleDialog.RESULT_RATIONALE_DIALOG,
//            this
//        ) { _, bundle ->
//            val activeFragment: Fragment? = childFragmentManager.findFragmentByTag(FRAGMENT_TAG_HOME)
//            (activeFragment as? HomeFragment)?.checkRationaleDialogPermissionResult(bundle)
//        }

//        parentFragmentManager.setFragmentResultListener(
//            KEY_APP_UPDATE_DIALOG,
//            this
//        ) { _, bundle ->
//            val result = bundle.getInt(KEY_ACTION_RESULT)
//            if (result == RESULT_UPDATE) {
//                activity?.let(viewModel::requestUpdateApp)
//            }
//        }

//        viewModel.navigationEvent.observe(this) {
//            when (it) {
//                NAVIGATION_DIALOG_UPDATE -> viewModel.showUpdateDialog(findNavController())
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_tab_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = view.findViewById(R.id.nav_view)
        bottomNavigationView?.setOnNavigationItemSelectedListener(navigationListener)

        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
//        viewModel.checkAppUpdate()
//
//        if(sessionPref.isLoggedIn) {
//            viewModel.checkUpdateUserConsent()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveBackStack()
    }

    private fun observeLiveData() {}

//    private fun updateBottomNavigationView(count: Int) {
//        if(count > 0) {
//            bottomNavigationView?.getOrCreateBadge(R.id.navigation_notification)?.also {
//                it.isVisible = true
//                it.number = count
//            }
//        } else {
//            bottomNavigationView
//                ?.getOrCreateBadge(R.id.navigation_notification)
//                ?.isVisible = false
//        }
//    }
//
//    private fun setStatusBarColor(@ColorInt color: Int) {
//        val context = requireActivity()
//        lifecycleScope.launchWhenCreated {
//            withContext(Dispatchers.Main) {
//                context.setStatusBarColor(color)
//                context.setStatusBarIconColor(color)
//            }
//        }
//    }

    private fun getSelectedMenu(@BottomNav.BottomNavType tabId: Int): Int = when (tabId) {
        TAB_PROMO -> R.id.navigation_promo
        TAB_ORDER_HISTORY -> R.id.navigation_order_history
        TAB_PROFILE -> R.id.navigation_profile
        else -> R.id.navigation_home
    }

    private fun loadFragment(@BottomNav.BottomNavType tabId: Int, firstInit: Boolean) {
        val fragment: Pair<Fragment, String> = when (tabId) {
            TAB_PROMO -> {
//                setStatusBarColor(BazaarColor.systemWhite)
                Pair(promoFragment, FRAGMENT_TAG_PROMO)
            }
            TAB_ORDER_HISTORY -> {
//                setStatusBarColor(BazaarColor.systemWhite)
                Pair(orderHistoryFragment, FRAGMENT_TAG_ORDER_HISTORY)
            }
            TAB_PROFILE -> {
//                setStatusBarColor(BazaarColor.systemWhite)
                Pair(profileFragment, FRAGMENT_TAG_PROFILE)
            }
            else -> {
//                setStatusBarColor(ColorToken.brand01)
                Pair(homeFragment, FRAGMENT_TAG_HOME)
            }
        }

        if(childFragmentManager.findFragmentByTag(fragment.second) == null) {
            childFragmentManager.beginTransaction().add(R.id.fragment_container, fragment.first, fragment.second).commit()
            activeFragment?.let { childFragmentManager.beginTransaction().hide(it).commit() }
        } else {
            activeFragment?.let {
                if(activeFragment != fragment.first)
                    childFragmentManager.beginTransaction().hide(it).show(fragment.first).commit()
            }
        }
        activeFragment = fragment.first
    }

    override fun onDismiss(dialogInterface: DialogInterface?) {
        val listener = childFragmentManager.findFragmentById(R.id.fragment_container) as? DialogInterface.OnDismissListener
        listener?.onDismiss(dialogInterface)
    }

    companion object {
        private const val FRAGMENT_TAG_HOME = "home"
        private const val FRAGMENT_TAG_PROMO = "promo"
        private const val FRAGMENT_TAG_ORDER_HISTORY = "order_history"
        private const val FRAGMENT_TAG_PROFILE = "profile"
    }
}