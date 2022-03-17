package com.haman.aop_part6_chapter01.data.repository.photo

import com.haman.aop_part6_chapter01.model.photo.PhotoModel

interface GalleryRepository {
    suspend fun getAllPhoto(): MutableList<PhotoModel>
}