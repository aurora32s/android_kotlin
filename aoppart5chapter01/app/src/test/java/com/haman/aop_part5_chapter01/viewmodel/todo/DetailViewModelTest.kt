package com.haman.aop_part5_chapter01.viewmodel.todo

import android.widget.ListView
import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.domain.todo.InsertToDoItemUseCase
import com.haman.aop_part5_chapter01.presentation.detail.DetailMode
import com.haman.aop_part5_chapter01.presentation.detail.DetailViewModel
import com.haman.aop_part5_chapter01.presentation.detail.ToDoDetailState
import com.haman.aop_part5_chapter01.presentation.list.ListViewModel
import com.haman.aop_part5_chapter01.presentation.list.ToDoListState
import com.haman.aop_part5_chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.lang.Exception

/**
 * [DetailViewModel]를 테스트하기 위한 Unit Test Class
 * 1. initData()
 * 2. test ViewModel fetch
 * 3. test Delete todo
 * 4. test Update todo
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class DetailViewModelTest: ViewModelTest() {

    private val id = 1L
    private val detailViewModel by inject<DetailViewModel>{ parametersOf(DetailMode.DETAIL, id)}
    private val listViewModel by inject<ListViewModel>()
    private val insertToDoItemUseCase : InsertToDoItemUseCase by inject()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    // init data by mocking
    private fun initData() = runTest {
        insertToDoItemUseCase(todo)
    }

    @Test
    fun `test viewModel fetch`() = runTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnIntialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(todo)
            )
        )
    }

    @Test
    fun `test Delete todo`() = runTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()

        detailViewModel.deleteToDo()

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnIntialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.toDoListLiveData.test()
        listViewModel.fetchData()

        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

    @Test
    fun `test Update todo`() = runTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        val updateTitle = "title 1 update"
        val updateDescription = "description 1 update"
        val updateToDo = todo.copy(
            title = updateTitle,
            description = updateDescription
        )
        detailViewModel.writeToDo(
            title = updateTitle,
            description = updateDescription
        )
        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnIntialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Success(updateToDo)
            )
        )
    }
}