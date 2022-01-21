package com.haman.aop_part2_chapter07

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton

/**
 * Custom View components
 * document 참조
 */
class RecordButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs) {
    // AppCompat : 매년 새로 출시되는 안드로이드 버전과 이전 버전과의 호환성을 위해 사용하는 라이브러리
    // layout xml : appcompat 에 매핑할 수 있는 클래스가 있는 경우, 컴파일러가 자동으로 매핑해준다.

    fun updateIconWithState (state: State) {
        when (state) { // with remaining chain
            State.BEFORE_RECORDING -> {
                // AppCompatImageButton 클래스의 setImageResource 메서드 호출
                setImageResource(R.drawable.ic_record)
            }
            State.ON_RECORDING,
            State.ON_PLAYING -> {
                setImageResource(R.drawable.ic_stop)
            }
            State.AFTER_RECORDING -> {
                setImageResource(R.drawable.ic_play)
            }
        }
    }
}