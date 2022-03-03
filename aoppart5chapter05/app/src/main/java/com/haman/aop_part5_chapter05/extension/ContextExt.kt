package com.haman.aop_part5_chapter05.extension

import android.content.Context
import androidx.annotation.Px

/**
 * 화면에 맞는 dp 값으로 변경
 */
@Px
fun Context.dip(dipValue: Float) = (dipValue * resources.displayMetrics.density).toInt()