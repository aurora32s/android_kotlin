package com.haman.aop_part2_chapter07

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(
    context: Context,
    attr : AttributeSet
) : AppCompatTextView(context, attr) {

    private var startTimeStamp : Long = 0L

    private val countUpAction : Runnable = object : Runnable {
        override fun run() {
            // TODO 시작할 때 타이머 시작
            val currentTimeStamp = SystemClock.elapsedRealtime()
            val countTimeSeconds = ((currentTimeStamp - startTimeStamp) / 1000L).toInt()
            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }

    fun startCountUp () {
        startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }

    fun stopCountUp () {
        handler?.removeCallbacks(countUpAction)
    }

    fun clearCountTime () {
        updateCountTime(0)
    }

    private fun updateCountTime (countTimeSecond : Int) {
        val minutes = countTimeSecond / 60
        val seconds = countTimeSecond % 60

        text = "%02d:%02d".format(minutes, seconds)
    }
}