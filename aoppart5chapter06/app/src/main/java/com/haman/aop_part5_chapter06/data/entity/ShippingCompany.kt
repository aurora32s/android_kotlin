package com.haman.aop_part5_chapter06.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 택배사
 */
@Entity
data class ShippingCompany(
    @PrimaryKey
    val code: String, // 택배사 코드
    val name: String // 택배사 명
)
