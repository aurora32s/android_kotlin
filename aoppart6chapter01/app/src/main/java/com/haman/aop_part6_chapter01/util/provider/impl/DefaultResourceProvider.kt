package com.haman.aop_part6_chapter01.util.provider.impl

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider

class DefaultResourceProvider(
    private val context: Context
) : ResourcesProvider {

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formArgs: Any): String = context.getString(resId, formArgs)

    override fun getColor(resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun getColorStateList(resId: Int): ColorStateList = context.getColorStateList(resId)

}