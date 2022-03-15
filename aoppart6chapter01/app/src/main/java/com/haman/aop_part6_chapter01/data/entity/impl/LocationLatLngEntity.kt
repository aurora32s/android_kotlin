package com.haman.aop_part6_chapter01.data.entity.impl

import android.os.Parcelable
import com.haman.aop_part6_chapter01.data.entity.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationLatLngEntity(
    val latitude: Double,
    val longitude: Double,
    override val id: Long = -1
): Entity, Parcelable
