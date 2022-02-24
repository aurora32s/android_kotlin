package com.haman.aop_part5_chapter01.domain.todo

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.data.repository.ToDoRepository
import com.haman.aop_part5_chapter01.domain.UseCase

class InsertToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {
    suspend operator fun invoke(todoItem: ToDoEntity): Long {
        return toDoRepository.insertToDo(todoItem)
    }
}