package com.haman.aop_part5_chapter01.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.domain.todo.DeleteToDoItemUseCase
import com.haman.aop_part5_chapter01.domain.todo.GetToDoItemUseCase
import com.haman.aop_part5_chapter01.domain.todo.InsertToDoItemUseCase
import com.haman.aop_part5_chapter01.domain.todo.UpdateToDoUseCase
import com.haman.aop_part5_chapter01.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

internal class DetailViewModel(
    var detailMode: DetailMode,
    var id: Long = -1,
    private val getToDoItemUseCase: GetToDoItemUseCase,
    private val deleteToDoItemUseCase: DeleteToDoItemUseCase,
    private val updateToDoUseCase: UpdateToDoUseCase,
    private val insertToDoItemUseCase: InsertToDoItemUseCase
): BaseViewModel() {

    private var _toDoDetailLiveData = MutableLiveData<ToDoDetailState>(ToDoDetailState.UnIntialized)
    val toDoDetailLiveData: LiveData<ToDoDetailState>
        get() = _toDoDetailLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
        when (detailMode) {
            DetailMode.WRITE -> {
                // TODO 나중에 작성모드로 상세화면 진입 로직 처리
                _toDoDetailLiveData.postValue(ToDoDetailState.Write)
            }
            DetailMode.DETAIL -> {
                try {
                    getToDoItemUseCase(id)?.let {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(it))
                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }
        }
    }

    fun deleteToDo() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
        try {
            val success = deleteToDoItemUseCase(id)
            if (success) {
                _toDoDetailLiveData.postValue(ToDoDetailState.Delete)
            } else {
                _toDoDetailLiveData.postValue(ToDoDetailState.Error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _toDoDetailLiveData.postValue(ToDoDetailState.Error)
        }
    }

    fun setModifyMode() = viewModelScope.launch {
        _toDoDetailLiveData.postValue(ToDoDetailState.Write)
    }

    fun writeToDo(title: String, description: String) = viewModelScope.launch {
        when (detailMode) {
            DetailMode.WRITE -> {
                // TODO 나중에 작성모드로 상세화면 진입 로직 처리
                try {
                    val todoEntity = ToDoEntity(
                        id = id,
                        title = title,
                        description = description
                    )
                    id = insertToDoItemUseCase(todoEntity)
                    _toDoDetailLiveData.postValue(ToDoDetailState.Success(todoEntity))
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }
            DetailMode.DETAIL -> {
                try {
                    _toDoDetailLiveData.postValue(ToDoDetailState.Loading)
                    getToDoItemUseCase(id)?.let {
                        val updateToDoEntity = it.copy(
                            title = title,
                            description = description
                        )
                        updateToDoUseCase(updateToDoEntity)
                        _toDoDetailLiveData.postValue(ToDoDetailState.Success(updateToDoEntity))
                    } ?: kotlin.run {
                        _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _toDoDetailLiveData.postValue(ToDoDetailState.Error)
                }
            }
        }
    }
}