package com.haman.aop_part3_chapter06.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haman.aop_part3_chapter06.AopPart5Chapter04Application.Companion.appContext
import kotlinx.coroutines.launch

class GalleryViewModel: ViewModel() {

    private val galleryPhotoRepository by lazy { GalleryPhotoRepository(appContext!!) }

    private lateinit var photoList: MutableList<GalleryPhoto>

    val galleryStateLiveDouble = MutableLiveData<GalleryState>(GalleryState.Unintialized)
    fun fetchData() = viewModelScope.launch {
        setState(GalleryState.Loading)
        photoList = galleryPhotoRepository.getAllPhoto()
        setState(
            GalleryState.Success(
                photoList
            )
        )
    }

    fun selectPhoto(galleryPhoto: GalleryPhoto) {
        val findGalleryPhoto = photoList.find { it.id == galleryPhoto.id }

        findGalleryPhoto?.let { photo ->
            photoList[photoList.indexOf(photo)] =
                photo.copy(
                    isSelected = photo.isSelected.not()
                )
            setState(
                GalleryState.Success(
                    photoList
                )
            )
        }
    }

    fun confirmCheckedPhoto() {
        setState(GalleryState.Loading)
        setState(
            GalleryState.Confirm(
                photoList = photoList.filter { it.isSelected }
            )
        )
    }

    private fun setState(state: GalleryState) {
        galleryStateLiveDouble.postValue(state)
    }
}