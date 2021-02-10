package com.klim.habrareader.app.managers.windows_manager.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import com.klim.habrareader.app.BaseFragment
import com.klim.habrareader.app.managers.windows_manager.Window
import com.klim.habrareader.app.managers.windows_manager.WindowsManager
import kotlin.math.abs


class WindowsContainer : FrameLayout {

    lateinit var windowsManager: WindowsManager
    var mDetector: GestureDetector

    private var startedSwipe = false
    private var startedSwipeDistanceX = 0f
    private var startedSwipeDistanceY = 0f

    private var enableUserSwipe = true
    private var displayWidth = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        mDetector = GestureDetector(context, GestureListener())

        displayWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
    }

    fun startWindow(fragment: BaseFragment, isItBase: Boolean = false) {
        windowsManager.startWindow(fragment, isItBase)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startedSwipe = false
                startedSwipeDistanceX = 0f
                startedSwipeDistanceY = 0f
            }
        }

        if (windowsManager.getTopWindow() != null && !windowsManager.getTopWindow()!!.base) {
            if (mDetector.onTouchEvent(ev))
                return true

            return false
        } else {
            return super.onInterceptTouchEvent(ev)
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if (mDetector.onTouchEvent(ev)) {
                return true
            } else {
                when (ev.action) {
                    MotionEvent.ACTION_UP -> {
                        startedSwipe = false
                        startedSwipeDistanceX = 0f
                        startedSwipeDistanceY = 0f

                        windowsManager.getTopWindow()?.let { window ->
                            val containerView = window.containerView
                            //close window
                            if (containerView.translationX > displayWidth / 5f) {
                                closeWindow(window)
                            } else {// return back window
                                moveBackWindow(window)
                            }
                        }
                    }
                    else -> {

                    }
                }
            }
        }

        return super.onTouchEvent(ev)
    }

    private fun moveBackWindow(window: Window) {
        val va = ValueAnimator.ofFloat(window.containerView.translationX, 0f)
        va.duration = 50
        va.addUpdateListener { animation ->
            enableUserSwipe = false
            window.containerView.translationX = animation.animatedValue as Float
        }
        va.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(
                animation: Animator?,
                isReverse: Boolean
            ) {
                super.onAnimationEnd(animation, isReverse)
                enableUserSwipe = true
            }
        })
        va.start()
    }

    private fun closeWindow(window: Window) {
        val va = ValueAnimator.ofFloat(
            window.containerView.translationX,
            displayWidth * 1.1f
        )
        va.duration = 200
        va.addUpdateListener { animation ->
            enableUserSwipe = false
            window.containerView.translationX = animation.animatedValue as Float
        }
        va.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                super.onAnimationEnd(animation, isReverse)
                enableUserSwipe = true
                windowsManager.onBackPressed()
            }
        })
        va.start()
    }

    internal inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(event: MotionEvent): Boolean {
            return super.onDown(event)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return super.onSingleTapUp(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return super.onSingleTapConfirmed(e)
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return super.onDoubleTap(e)
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (enableUserSwipe) {
                startedSwipeDistanceX += distanceX
                startedSwipeDistanceY += distanceY
                startedSwipe =
                    startedSwipe || (abs(startedSwipeDistanceX) > abs(startedSwipeDistanceY) * 3 && abs(
                        startedSwipeDistanceX
                    ) > 100) // todo change 100 to 32dp
                if (startedSwipe) {

                    windowsManager.getTopWindow()?.let { window ->
                        val containerView = window.containerView
                        if (containerView.translationX - distanceX > 0) {
                            containerView.translationX = containerView.translationX - distanceX
                        }
                    }

                    return true
                }

            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            return super.onFling(event1, event2, velocityX, velocityY)
        }
    }

    fun onBackPressed(): Boolean {
        val result = windowsManager.checkBackPressed()
        if (result) {
            windowsManager.getTopWindow()?.let {
                closeWindow(it)
            }
        }
        return result
    }
}