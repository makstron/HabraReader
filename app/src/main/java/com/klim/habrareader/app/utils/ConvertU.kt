package com.klim.habrareader.app.utils

import android.util.TypedValue
import com.klim.habrareader.App

object ConvertU {
    fun convertDpToPixels(dp: Float): Int {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getApp().resources.displayMetrics))
    }

    fun convertSpToPixels(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, App.getApp().resources.displayMetrics).toInt()
    }

    fun convertDpToSp(dp: Float): Int {
        return (convertDpToPixels(dp) / convertSpToPixels(dp).toFloat()).toInt()
    }
}