package com.mib.feature_home.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    private var mListFragment: MutableList<Fragment> = ArrayList()
    private var mTitles: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return mListFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return mListFragment[position]
    }

    fun addFragment(fragment: Fragment, title: String = "") {
        mListFragment.add(fragment)
        mTitles.add(title)
    }

    fun getPageTitle(position: Int): String {
        return mTitles[position]
    }

}