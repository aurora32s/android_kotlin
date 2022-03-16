package com.haman.aop_part6_chapter01.util.event

import com.haman.aop_part6_chapter01.screen.main.MainTabMenu
import kotlinx.coroutines.flow.MutableSharedFlow

class MenuChangeEventBus {

    val mainTabMenuFlow = MutableSharedFlow<MainTabMenu>()

    suspend fun changeMenu(menu: MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }

}