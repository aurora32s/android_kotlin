package com.haman.aop_part4_chapter06.data.models.airquality

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import com.haman.aop_part4_chapter06.R

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResId: Int
) {
    @SerializedName("1")
    GOOD("์ข์","๐", R.color.blue),
    @SerializedName("2")
    NORMAL("๋ณดํต","๐", R.color.green),
    @SerializedName("3")
    BAD("๋์จ", "โน๏ธ", R.color.yellow),
    @SerializedName("4")
    AWFUL("๋งค์ฐ ๋์จ", "๐ซ", R.color.red),

    UNKNOWN("๋ฏธ์ธก์ ", "๐คจ", R.color.gray);

    override fun toString(): String {
        return "$label $emoji"
    }
}