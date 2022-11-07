package com.mystic.koffee.petreport.support.ui

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mystic.koffee.petreport.R

class CustomSwipeRefreshLayout : SwipeRefreshLayout {

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init() }

    private fun init() {
        setColorSchemeColors(ContextCompat.getColor(context, R.color.primaryColor))
    }
}