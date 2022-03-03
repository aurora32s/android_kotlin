package com.haman.aop_part5_chapter06.data.entity

import androidx.room.Embedded
import androidx.room.Entity

/**
 * Room 에 저장되는 객체
 */
@Entity(primaryKeys = ["invoice", "code"])
data class TrackingItem(
    val invoice: String, // 운송장 번호
    @Embedded val company: ShippingCompany // 택배사
)
