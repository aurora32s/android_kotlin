package com.haman.aop_part5_chapter05.presentation

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface BasePresenter {
    val scope: CoroutineScope
        get() = MainScope()

    /**
     * presenter -> View
     */
    fun onViewCreated()

    fun onDestroyView()

    /**
     * 실무에서는 life cycle 에 자동으로 호출될 수 있도록
     * base fragment 를 생성해서 scope 를 관리해준다.
     * 해당 프로젝트에서는 fragment 마다 life cycle 과 관련된
     * 메서드를 직접 호출해준다.
     */
    @CallSuper
    fun onDestroy() {
        scope.cancel()
    }
}