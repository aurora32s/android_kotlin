package com.haman.aop_part6_chapter01.data.entity.impl

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.haman.aop_part6_chapter01.data.entity.Entity
import kotlinx.parcelize.Parcelize

@androidx.room.Entity
@Parcelize
data class LocationLatLngEntity(
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey(autoGenerate = true)
    override val id: Long = -1
): Entity, Parcelable
