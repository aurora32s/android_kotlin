package com.haman.aop_part2_chapter07

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.random.Random

class SoundVisualizerView(
    context: Context,
    attr: AttributeSet
) : View(context, attr) {

    var onRequestCurrentAmplitude : (() -> Int)? = null

    // alias : 일러스트의 안티에일리어싱와 비슷하게 디지털 신호가 부드럽게 보이도록 처리
    // paint : 어떻게 그릴지 설정
    // onDraw 메서드는 자주 호출되기 때문에, Paint 객체는 별도로 전역에 선언해주자.
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.white)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND // 선의 끝을 어떻게 표현해줄 것인가
    }
    private var drawingWidth : Int = 0
    private var drawingHeight : Int = 0
    // 녹음 높낮이 저장
    private var drawingAmplitudes : List<Int> = emptyList()
    private var isReplaying : Boolean = false
    private var replayingPosition : Int = 0 // 재생하는 위치

    private val visualizeRepeatAction : Runnable = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                // TODO Amplitude 를 이용해 draw 요청
                // MainActivity 에서 구현해준 함수를 실행
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }
            invalidate() // onDraw 를 다시 호출

            // 해당 thread 를 20ms 뒤에 다시 시작
            // 람다에서는 this 를 사용할 수 없기 때문에 무명 클래스를 구현해야 합니다.
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

    // 실제 그림을 그리는 view
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        val centerY = drawingHeight / 2f // 중앙 높이
        var offsetX = drawingWidth.toFloat() // 초기 시작점은 오른쪽

        drawingAmplitudes
            .let { amplitudes ->
                if (isReplaying) { // ON_PLAYING
                    amplitudes.takeLast(replayingPosition)
                } else {
                    amplitudes
                }
            }.forEach { amplitude ->
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.7F // 최대 길이에 대한 비율로

            offsetX -= LINE_SPACE

            if (offsetX < 0) { // 화면 바깥으로 벗어난 경우부터는 그리지 않는다.
                return@forEach
            }
            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2F,
                offsetX,
                centerY + lineLength / 2F,
                amplitudePaint
            )
        }
    }

    fun startVisualizing (isReplaying : Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing () {
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun resetAmplitudes () {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        // 나누었을 때 값이 0(몫만 반환)이 되는 것을 방비
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }
}