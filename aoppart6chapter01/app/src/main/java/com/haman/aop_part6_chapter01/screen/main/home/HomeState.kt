package com.haman.aop_part6_chapter01.screen.main.home

import androidx.annotation.StringRes
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity

sealed class HomeState {

    object UnInitialized: HomeState()

    object Loading: HomeState()

    data class Success(
        val mapSearchInfoEntity: MapSearchInfoEntity
    ): HomeState()

    data class Error(
        @StringRes val messageId: Int
    ): HomeState()
}