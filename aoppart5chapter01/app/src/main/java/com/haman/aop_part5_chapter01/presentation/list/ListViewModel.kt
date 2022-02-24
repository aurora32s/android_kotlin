package com.haman.aop_part5_chapter01.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.domain.todo.*
import com.haman.aop_part5_chapter01.domain.todo.GetToDoListUseCase
import com.haman.aop_part5_chapter01.domain.todo.UpdateToDoUseCase
import com.haman.aop_part5_chapter01.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.experimental.property.inject

/**
 * 필요한 UseCase
 * 1. [GetTodoListUseCase]
 * 2. [UpdateTodoUseCase]
 * 3. [DeleteAllTodoItemUseCase]
 */
internal class ListViewModel(
    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val deleteAllToDoUseCase: DeleteAllToDoUseCase
): BaseViewModel() {
    private var _toDoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val toDoListLiveData: LiveData<ToDoListState>
        get() = _toDoListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }

    fun updateEntity(toDoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoUseCase(toDoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        _toDoListLiveData.postValue(ToDoListState.Loading)
        deleteAllToDoUseCase()
        _toDoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }
}