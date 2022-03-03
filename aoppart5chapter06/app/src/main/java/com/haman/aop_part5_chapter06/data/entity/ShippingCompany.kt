package com.haman.aop_part5_chapter06.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * 택배사
 */
@Parcelize
@Entity
data class ShippingCompany(
    @PrimaryKey
    @SerializedName("Code")
    val code: String, // 택배사 코드
    @SerializedName("Name")
    val name: String // 택배사 명
): Parcelable
