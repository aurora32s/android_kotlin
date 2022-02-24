package com.haman.aop_part5_chapter01.data.repository

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity

class TestToDoRepository : ToDoRepository {

    // test 용 cache list
    private val toDoList: MutableList<ToDoEntity> = mutableListOf()

    override suspend fun getToDoList(): List<ToDoEntity> = toDoList

    override suspend fun insertToDoList(todoList: List<ToDoEntity>) {
        this.toDoList.addAll(todoList)
    }

    override suspend fun insertToDo(todoItem: ToDoEntity): Long {
        this.toDoList.add(todoItem)
        return todoItem.id
    }

    override suspend fun updateToDoItem(toDoEntity: ToDoEntity): Boolean {
        val foundToDoEntity = toDoList.find { it.id == toDoEntity.id }

        if (foundToDoEntity == null) {
            return false
        }
        else {
            this.toDoList[toDoList.indexOf(foundToDoEntity)] = toDoEntity
            return true
        }
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? {
        return toDoList.find { it.id == itemId }
    }

    override suspend fun deleteAll() {
        toDoList.clear()
    }

    override suspend fun deleteToDoItem(id: Long): Boolean {
        val foundToDoEntity = toDoList.find { it.id == id }

        if (foundToDoEntity == null) {
            return false
        }
        else {
            this.toDoList.removeAt(toDoList.indexOf(foundToDoEntity))
            return true
        }
    }
}