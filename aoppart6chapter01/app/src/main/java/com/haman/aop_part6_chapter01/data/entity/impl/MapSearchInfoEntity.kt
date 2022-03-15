package com.haman.aop_part6_chapter01.data.entity.impl

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapSearchInfoEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
): Parcelable
