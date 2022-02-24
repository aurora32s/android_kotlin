package com.haman.aop_part5_chapter01.presentation.list

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity

// sealed : ToDoListState 를 상속받은 Object 관리
sealed class ToDoListState {

    object UnInitialized: ToDoListState()
    object Loading: ToDoListState()

    data class Success(
        val toDoList: List<ToDoEntity>
    ): ToDoListState()

    object Error: ToDoListState()
}