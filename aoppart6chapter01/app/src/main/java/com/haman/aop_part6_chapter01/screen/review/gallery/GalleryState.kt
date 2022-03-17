package com.haman.aop_part6_chapter01.screen.review.gallery

import com.haman.aop_part6_chapter01.model.photo.PhotoModel

sealed class GalleryState {
    object Unintialized: GalleryState()

    object Loading: GalleryState()

    data class Success(
        val photoList: List<PhotoModel>
    ): GalleryState()

    data class Confirm(
        val photoList: List<PhotoModel>
    ): GalleryState()

    object Error: GalleryState()
}