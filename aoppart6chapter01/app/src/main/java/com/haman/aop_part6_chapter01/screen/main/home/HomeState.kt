package com.haman.aop_part6_chapter01.screen.main.home

sealed class HomeState {

    object UnInitialized: HomeState()

    object Loading: HomeState()

    object Success: HomeState()

}