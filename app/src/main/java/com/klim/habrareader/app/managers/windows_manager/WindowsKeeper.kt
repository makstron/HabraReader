package com.klim.habrareader.app.managers.windows_manager

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import com.klim.habrareader.app.windows.BaseFragment
import java.lang.Exception
import java.util.*

class WindowsKeeper(var activity: WindowsContainerActivity) {
    private var windows = ArrayList<Window>(3)

    @SuppressLint("ClickableViewAccessibility")
    fun startWindow(fragment: BaseFragment, isItBase: Boolean = false) {
        val containerView = activity.getActivityViewContainer()

        val frameLayout = FrameLayout(activity.getContext())
        frameLayout.id = View.generateViewId()
        frameLayout.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        containerView.addView(frameLayout)

        setFragment(activity, frameLayout.id, fragment, fragment::class.java.simpleName)
        windows.add(Window(fragment, frameLayout, isItBase))
    }

    private fun setFragment(activity: WindowsContainerActivity, targetId: Int, fragment: BaseFragment, fragmentTag: String?) {
        val fragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
        fragmentTransaction.add(targetId, fragment, fragmentTag)
        fragmentTransaction.commit()
    }

    private fun removeView(window: Window) {
        val containerView = activity.getActivityViewContainer()
        containerView.removeView(window.containerView)
    }

    private fun removeFragment(window: Window) {
        val fragmentTransaction = activity.getSupportFragmentManager().beginTransaction()
        fragmentTransaction.remove(window.fragment)
        fragmentTransaction.commit()
    }

    fun getTopWindow(): Window {
        return if (windows.size > 0)
            windows.last()
        else
            throw Exception("Window was not found. Windows list is empty")
    }

    fun getTopWindowOrNull(): Window? {
        return if (windows.size > 0)
            windows.last()
        else
            null
    }

    fun existsWindowForBackPressed(): Boolean = !getTopWindow().base

    fun removeTopWindow(): Boolean {
        if (existsWindowForBackPressed()) {
            val topWindow = getTopWindow()
            removeFragment(topWindow)
            removeView(topWindow)
            windows.removeLast()
            return true
        }
        return false
    }
}