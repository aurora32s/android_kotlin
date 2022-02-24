package com.haman.aop_part5_chapter01.domain.todo

import com.haman.aop_part5_chapter01.data.repository.ToDoRepository
import com.haman.aop_part5_chapter01.domain.UseCase

class DeleteToDoItemUseCase(
    private val toDoRepository: ToDoRepository
): UseCase {

    suspend operator fun invoke(id: Long): Boolean {
        return toDoRepository.deleteToDoItem(id)
    }
}