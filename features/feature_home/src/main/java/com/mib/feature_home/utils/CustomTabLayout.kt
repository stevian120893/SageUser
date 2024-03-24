package com.mib.feature_home.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.mib.feature_home.R

class CustomTabLayout: FrameLayout {

    companion object {
        /**
         * Display all tabs with equal width.
         */
        const val TAB_MODE_EQUAL = 0

        /**
         * Display all tabs stick to all sides.
         */
        const val TAB_MODE_STICKY_SIDE = 1

        /**
         * Display all tabs wrap to the content.
         */
        const val TAB_MODE_WRAP = 2
    }

    private lateinit var mViewIndicator: View

    private var mBaseLayout: RelativeLayout
    private var mTabLayout: LinearLayout
    private var mListTextLayout: MutableList<FrameLayout> = ArrayList()
    private var mListTextView: MutableList<TextView> = ArrayList()
    private var mOnTabSelectedListener: OnTabSelectedListener? = null
    var selectedTabPosition = 0
    var tabMode = TAB_MODE_EQUAL

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int)
    }

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout)
        tabMode = a.getInteger(R.styleable.CustomTabLayout_tab_mode, TAB_MODE_EQUAL)
        a.recycle()

        mBaseLayout = RelativeLayout(context)
        super.addView(mBaseLayout, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        mTabLayout = LinearLayout(context)
        mTabLayout.id = View.generateViewId()
        mTabLayout.orientation = LinearLayout.HORIZONTAL
        mBaseLayout.addView(mTabLayout, RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        mViewIndicator = View(context)
        mViewIndicator.background = ContextCompat.getDrawable(context, R.drawable.bg_indicator)
        val viewIndicatorLP = RelativeLayout.LayoutParams(0, context.resources.getDimensionPixelSize(R.dimen.indicator_height))
//        viewIndicatorLP.setMargins(0,20,0,0)
        viewIndicatorLP.topMargin = context.resources.getDimensionPixelSize(R.dimen._5sdp)
        viewIndicatorLP.addRule(RelativeLayout.BELOW, mTabLayout.id)
        mViewIndicator.layoutParams = viewIndicatorLP
        mBaseLayout.addView(mViewIndicator)
    }

    fun setupWithViewPager(viewPager: ViewPager2, adapter: TabAdapter) {
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = adapter.itemCount - 1
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeTab(position)
            }
        })

        mOnTabSelectedListener = object: OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                viewPager.currentItem = position
            }
        }

        mListTextLayout = ArrayList()
        mListTextView = ArrayList()
        for(i in 0 until (viewPager.adapter?.itemCount ?: 0)) {
            addTab(adapter.getPageTitle(i))
        }

        mBaseLayout.post {
            changeTab(0)
        }
    }

    private fun createTextView(title: String, position: Int): FrameLayout {
        val textLayout = FrameLayout(context)
        when(tabMode) {
            TAB_MODE_EQUAL, TAB_MODE_STICKY_SIDE -> {
                val textLayoutLP = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT)
                textLayoutLP.weight = 1f
                if(position > 0) {
                    textLayoutLP.marginStart = context.resources.getDimensionPixelSize(R.dimen._16sdp)
                }
                textLayout.layoutParams = textLayoutLP
            }
            TAB_MODE_WRAP -> {
                val textLayoutLP = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                if(position > 0) {
                    textLayoutLP.marginStart = context.resources.getDimensionPixelSize(R.dimen._16sdp)
                }
                textLayout.layoutParams = textLayoutLP
            }
        }

        val textView = TextView(context)
        textView.id = View.generateViewId()
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.TextViewTabTextAppearance)
        } else {
            textView.setTextAppearance(context, R.style.TextViewTabTextAppearance)
        }
        //textView.includeFontPadding = false
        textView.text = title
        textView.textSize = 14f
        val textViewLP = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textViewLP.topMargin = context.resources.getDimensionPixelSize(R.dimen._5sdp)
        textViewLP.gravity = Gravity.CENTER_HORIZONTAL
        textView.layoutParams = textViewLP

        if(tabMode == TAB_MODE_STICKY_SIDE) {
            if(position == 1) {
                textView.gravity = Gravity.END
            }
        } else {
            textView.gravity = Gravity.CENTER
        }
        mListTextView.add(textView)
        textLayout.addView(textView)

        textLayout.setOnClickListener {
            changeTab(position)
            mOnTabSelectedListener?.onTabSelected(position)
        }

        return textLayout
    }

    fun changeTab(position: Int) {
        selectedTabPosition = position

        val textLayout = mListTextLayout[position]
        val textView = mListTextView[position]
        var indicatorWidth = resources.getDimensionPixelSize(R.dimen.indicator_width)
        val x = textLayout.x + textView.x
        val centerX = x + textView.width / 2 - indicatorWidth / 2
        var startPosition = centerX
        if(textView.width < indicatorWidth) {
            startPosition = x + resources.getDimensionPixelSize(R.dimen._5sdp)
        }
        val objectAnimator = ObjectAnimator.ofFloat(mViewIndicator, "translationX", startPosition.toFloat())
        objectAnimator.duration = 200
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.start()

        if(textView.width < indicatorWidth) {
            indicatorWidth = textView.width - (resources.getDimensionPixelSize(R.dimen._5sdp) * 2)
        }
        val valueAnimator = ValueAnimator.ofInt(mViewIndicator.width, indicatorWidth)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 200
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Int
            val viewIndicatorLp = mViewIndicator.layoutParams as RelativeLayout.LayoutParams
            viewIndicatorLp.width = progress
            mViewIndicator.layoutParams = viewIndicatorLp
        }
        valueAnimator.start()
    }

    fun addTab(title: String) {
        val textLayout = createTextView(title, mListTextLayout.size)
        mListTextLayout.add(textLayout)
        mTabLayout.addView(textLayout)

        if(mListTextLayout.size == 1) {
            mBaseLayout.post {
                changeTab(0)
            }
        }
    }

    fun addTabs(listTitle: List<String>) {
        listTitle.forEach { addTab(it) }
    }

}