package com.klim.habrareader.app.managers.windows_manager.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import com.klim.habrareader.R
import com.klim.habrareader.app.windows.BaseFragment
import com.klim.habrareader.app.managers.windows_manager.Window
import com.klim.habrareader.app.managers.windows_manager.WindowsKeeper
import kotlin.math.abs


class WindowsContainer : FrameLayout {

    lateinit var windowsKeeper: WindowsKeeper
    private var mDetector: GestureDetector

    private var startedClosing = false
    private var swipeDistance: PointF = PointF(0f, 0f)

    private var enableUserSwipe = true
    private var displayWidth = 0f
    private var moveDistanceBeforeStartSwipe = 0f
    private var xMoreThanYScrollForStartSwipe = 3f //swipe horizontal more than swipe vertical in this count of times for start closing window. It is like a filter
    private var pointOfNoReturn = 0f //after this swipe distance window will be close

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        mDetector = GestureDetector(context, GestureListener())
        displayWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()
        moveDistanceBeforeStartSwipe = context.resources.getDimension(R.dimen.start_swipe_distance)
        pointOfNoReturn = displayWidth / 5f
    }

    fun startWindow(fragment: BaseFragment, isItBase: Boolean = false) {
        windowsKeeper.startWindow(fragment, isItBase)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startedClosing = false
                resetSwipeDistance()
            }
        }

        return if (!windowsKeeper.getTopWindow().base) {
            mDetector.onTouchEvent(ev)
        } else {
            super.onInterceptTouchEvent(ev)
        }
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if (mDetector.onTouchEvent(ev)) {
                return true
            } else {
                when (ev.action) {
                    MotionEvent.ACTION_UP -> {
                        startedClosing = false
                        resetSwipeDistance()
                        finishWindowSwiping()
                    }
                }
            }
        }

        return super.onTouchEvent(ev)
    }

    private fun resetSwipeDistance() {
        swipeDistance = PointF(0f, 0f)
    }

    private fun finishWindowSwiping() {
        val topWindow = windowsKeeper.getTopWindow()
        if (topWindow.containerView.translationX > pointOfNoReturn) {
            startWindowClosingAnimation(topWindow)
        } else {
            moveBackWindow(topWindow)
        }
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

    private fun startWindowClosingAnimation(window: Window) {
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
                windowsKeeper.removeTopWindow()
            }
        })
        va.start()
    }

    fun onBackPressed(): Boolean {
        val result = windowsKeeper.existsWindowForBackPressed()
        if (result) {
            closeTopWindow()
        }
        return result
    }

    private fun closeTopWindow() {
        startWindowClosingAnimation(windowsKeeper.getTopWindow())
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
                addToSwipeDistance(distanceX, distanceY)
                checkConditionsForStartClosing()
                if (startedClosing) {
                    moveTopWindow(distanceX, distanceY)
                    return true
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        private fun addToSwipeDistance(distanceX: Float, distanceY: Float) {
            swipeDistance.x += distanceX
            swipeDistance.y += distanceY
        }

        private fun checkConditionsForStartClosing() {
            startedClosing = startedClosing || (
                    abs(swipeDistance.x) > abs(swipeDistance.y) * xMoreThanYScrollForStartSwipe &&
                            abs(swipeDistance.x) > moveDistanceBeforeStartSwipe)
        }

        private fun moveTopWindow(distanceX: Float, distanceY: Float) {
            val containerView = windowsKeeper.getTopWindow().containerView
            if (containerView.translationX - distanceX > 0) {
                containerView.translationX = containerView.translationX - distanceX
            }
        }

        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            return super.onFling(event1, event2, velocityX, velocityY)
        }
    }
}