package com.haman.aop_part5_chapter02.extensions

import android.content.res.Resources

internal fun Float.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}