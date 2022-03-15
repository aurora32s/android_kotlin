package com.haman.aop_part6_chapter01.screen.mylocation

import androidx.annotation.StringRes
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity

sealed class MyLocationState {

    object UnInitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Confirm(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): MyLocationState()

    data class Error(
        @StringRes val messageId: Int
    ): MyLocationState()
}