package com.haman.aop_part6_chapter01.screen.main.home

import androidx.lifecycle.MutableLiveData
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel

class HomeViewModel: BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.UnInitialized)
    val homeStateLiveData
        get() = _homeStateLiveData

}