package com.haman.aop_part5_chapter07.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

/**
 * 영화
 * fire store 에 사용되는 객체는
 * default 값을 설정해주어야 한다.
 */
@Parcelize
data class Movie(
    @DocumentId
    val id: String? = null,

    @field:JvmField
    val isFeatured: Boolean? = null,

    val title: String? = null,
    val actors: String? = null,
    val country: String? = null,
    val director: String? = null,
    val genre: String? = null,
    val posterUrl: String? = null,
    val rating: String? = null,
    val averageScore: Float? = null,
    val numberOfScore: Int? = null,
    val releaseYear: Int? = null,
    val runtime: Int? = null
) : Parcelable
