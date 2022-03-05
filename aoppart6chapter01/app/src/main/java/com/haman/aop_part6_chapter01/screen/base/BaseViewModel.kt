package com.haman.aop_part6_chapter01.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel Base Class
 */
abstract class BaseViewModel: ViewModel() {
    // life cycle 과 관련된 상태값들을 저장
    protected var stateBundle: Bundle? = null

    // 화면에 필요한 데이터 요청
    open fun fetchData(): Job = viewModelScope.launch {  }
    // 완전히 view 가 destroy 가 되기 전까지 bundle 보관
    open fun storeState(stateBundle: Bundle) {
        this.stateBundle = stateBundle
    }
}