package com.haman.aop_part5_chapter06.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

/**
 * Room 에 저장되는 객체
 */
@Parcelize
@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(
    val invoice: String, // 운송장 번호
    @Embedded val company: ShippingCompany // 택배사
): Parcelable
