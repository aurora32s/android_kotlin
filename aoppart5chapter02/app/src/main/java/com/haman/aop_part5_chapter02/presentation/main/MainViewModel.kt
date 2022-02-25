package com.haman.aop_part5_chapter02.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part5_chapter02.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MainViewModel: BaseViewModel() {
    override fun fetchData(): Job = viewModelScope.launch {}

    private var _mainStateLiveData = MutableLiveData<MainState>()
    val mainStateLiveData
        get() = _mainStateLiveData

    fun refreshOrderList() = viewModelScope.launch {
        _mainStateLiveData.postValue(MainState.RefreshOrderList)
    }
}