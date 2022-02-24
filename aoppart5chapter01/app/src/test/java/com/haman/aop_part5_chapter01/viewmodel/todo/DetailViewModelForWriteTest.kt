package com.haman.aop_part5_chapter01.viewmodel.todo

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.presentation.detail.DetailMode
import com.haman.aop_part5_chapter01.presentation.detail.DetailViewModel
import com.haman.aop_part5_chapter01.presentation.detail.ToDoDetailState
import com.haman.aop_part5_chapter01.presentation.list.ListViewModel
import com.haman.aop_part5_chapter01.presentation.list.ToDoListState
import com.haman.aop_part5_chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

/**
 * [DetailViewModel]를 테스트하기 위한 Unit Test Class
 * 1. test ViewModel fetch
 * 2. test Insert todo
 */
internal class DetailViewModelForWriteTest: ViewModelTest() {

    private val id = 1L
    private val detailViewModel by inject<DetailViewModel>{ parametersOf(DetailMode.WRITE, id) }
    private val listViewModel by inject<ListViewModel>()

    private val todo = ToDoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Test
    fun `test viewModel fetch`() = runTest {
        val testObservable = detailViewModel.toDoDetailLiveData.test()
        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnIntialized,
                ToDoDetailState.Loading,
                ToDoDetailState.Write
            )
        )
    }

    @Test
    fun `test Insert todo`() = runTest {
        val detailTestObservable = detailViewModel.toDoDetailLiveData.test()

        detailViewModel.writeToDo(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                ToDoDetailState.UnIntialized,
                ToDoDetailState.Success(todo)
            )
        )

        assert(detailViewModel.detailMode == DetailMode.WRITE)
        assert(detailViewModel.id == id)
        val listTestObservable = listViewModel.toDoListLiveData.test()
        listViewModel.fetchData()

        listTestObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf(todo))
            )
        )
    }
}