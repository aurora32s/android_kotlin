package com.haman.aop_part5_chapter05.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Station(
    val name: String,
    val isFavorite: Boolean,
    val connectedSubways: List<Subway>
): Parcelable