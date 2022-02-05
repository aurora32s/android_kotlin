package com.haman.aop_part3_chapter08

import android.app.Notification
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.math.abs

class VideoMotionLayout(
    context : Context,
    attrs : AttributeSet? = null
) : MotionLayout(context, attrs) {

    private var motionTouchedStarted = false
    private val mainContainerView by lazy {
        findViewById<View>(R.id.mainContainerView)
    }
    private val hitRect = Rect()

    init {
        setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?,startId: Int,endId: Int) {}
            override fun onTransitionTrigger(motionLayout: MotionLayout?,triggerId: Int,positive: Boolean,progress: Float) {}

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // fragment 가 attach 되어 있는 activity
                (context as MainActivity).also { mainActivity ->
                    mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress =
                        abs(progress)
                }
            }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                motionTouchedStarted = false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                motionTouchedStarted = false
                return super.onTouchEvent(event) // 기존의 touch event 그대로 수행
            }
        }

        if (!motionTouchedStarted) {
            mainContainerView.getHitRect(hitRect)
            // mainContainerView 내에서 발생한 event 인 경우, motionTouchedStarted true 로 변경
            motionTouchedStarted = hitRect.contains(event.x.toInt(), event.y.toInt())
        }

        // mainContainerView 내부에서 터치가 발생한 경우에만, 애니메이션 실행
        return super.onTouchEvent(event) && motionTouchedStarted
    }

    private val gestureListener by lazy {
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                mainContainerView.getHitRect(hitRect)
                return hitRect.contains(e1.x.toInt(), e1.y.toInt())
            }
        }
    }

    private val gestureDetector by lazy {
        GestureDetector(context, gestureListener)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
}