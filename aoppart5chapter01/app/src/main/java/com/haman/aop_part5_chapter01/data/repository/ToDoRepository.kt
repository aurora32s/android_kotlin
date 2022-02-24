package com.haman.aop_part5_chapter01.data.repository

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity

/**
 * 1. insertToDoList
 * 2. getToDoList
 * 3. updateToDoItem
 */
interface ToDoRepository {

    suspend fun getToDoList(): List<ToDoEntity>

    suspend fun insertToDoList(todoList: List<ToDoEntity>)

    suspend fun insertToDo(todoItem: ToDoEntity): Long

    suspend fun updateToDoItem(toDoEntity: ToDoEntity): Boolean

    suspend fun getToDoItem(itemId: Long): ToDoEntity?

    suspend fun deleteAll()

    suspend fun deleteToDoItem(id: Long): Boolean
}