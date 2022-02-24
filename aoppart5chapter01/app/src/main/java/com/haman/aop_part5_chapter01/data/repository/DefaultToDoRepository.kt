package com.haman.aop_part5_chapter01.data.repository

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.data.local.db.dao.ToDoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultToDoRepository(
    private val toDoDao: ToDoDao,
    private val ioDispatcher: CoroutineDispatcher
) : ToDoRepository {

    override suspend fun getToDoList(): List<ToDoEntity> = withContext(ioDispatcher) {
        toDoDao.getAll()
    }
    override suspend fun getToDoItem(itemId: Long): ToDoEntity? = withContext(ioDispatcher) {
        toDoDao.getById(itemId)
    }
    override suspend fun insertToDoList(todoList: List<ToDoEntity>) = withContext(ioDispatcher) {
        toDoDao.insert(todoList)
    }
    override suspend fun insertToDo(todoItem: ToDoEntity): Long = withContext(ioDispatcher) {
        toDoDao.insert(todoItem)
    }
    override suspend fun updateToDoItem(toDoEntity: ToDoEntity): Boolean =
        withContext(ioDispatcher) {
            try {
                toDoDao.update(toDoEntity)
                true
            } catch (e: Exception) {
                false
            }
        }
    override suspend fun deleteAll() = withContext(ioDispatcher) {
        toDoDao.deleteAll()
    }
    override suspend fun deleteToDoItem(id: Long): Boolean = withContext(ioDispatcher) {
        try {
            toDoDao.delete(id)
            true
        } catch (e: Exception) {
            false
        }
    }
}