package com.haman.aop_part5_chapter01.presentation.detail

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity

sealed class ToDoDetailState {

    object UnIntialized: ToDoDetailState()
    object Loading: ToDoDetailState()

    data class Success(
        val todoItem: ToDoEntity
    ): ToDoDetailState()

    object Delete: ToDoDetailState()
    object Modify: ToDoDetailState()
    object Error: ToDoDetailState()
    object Write: ToDoDetailState()
}