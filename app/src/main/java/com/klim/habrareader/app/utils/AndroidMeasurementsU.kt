package com.klim.habrareader.app.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.Window

object AndroidMeasurementsU {

    var statusBarHeight = -1

    fun getStatusBarHeight(activity: Activity, def: Int): Int {
        if (statusBarHeight == -1) {
            statusBarHeight = try {
                val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    activity.resources.getDimensionPixelSize(resourceId)
                } else {
                    val h = getStatusBarHeight(activity)
                    if (h != 0) {
                        h
                    } else {
                        return def
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return def
            }
        }
        return statusBarHeight
    }

    fun getStatusBarHeight(activity: Activity): Int {
        val rectangle = Rect()
        val window = activity.window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        return contentViewTop - statusBarHeight
    }

}