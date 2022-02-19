package com.haman.aop_part5_chapter05.presentation

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface BasePresenter {

    val scope: CoroutineScope
        get() = MainScope()

    // fragment 마다 직접 호출
    // 실무에서는 Lifecycle 에 맞춰 호출해주는 base fragment 를 생성한다.
    fun onViewCreated()

    fun onDestroyView()

    @CallSuper
    fun onDestroy() {
        scope.cancel()
    }
}