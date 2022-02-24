package com.haman.aop_part5_chapter01.viewmodel.todo

import com.haman.aop_part5_chapter01.data.entity.ToDoEntity
import com.haman.aop_part5_chapter01.domain.todo.GetToDoItemUseCase
import com.haman.aop_part5_chapter01.domain.todo.InsertToDoListUseCase
import com.haman.aop_part5_chapter01.presentation.list.ListViewModel
import com.haman.aop_part5_chapter01.presentation.list.ToDoListState
import com.haman.aop_part5_chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

/**
 * [ListViewModel]를 테스트하기 위한 Unit Test Class
 * 추가한 아이템이 리스트에 맞게 표시되고 있는지 테스트
 *
 * 1. InitData(): Mocking Data
 * 2. test viewModel fetch: 데이터가 정상적으로 load 되는지 확인
 * 3. test Item Update
 * 4. test Item Delete ALL
 */
@ExperimentalCoroutinesApi
internal class ListViewModelTest: ViewModelTest() {

    // DI
    private val viewModel: ListViewModel by inject()
    private val insertToDoUseCase: InsertToDoListUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val mockList = (0 until 9).map {
        ToDoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }

    /**
     * 필요한 UseCase
     * 1. InsertTodoListUseCase
     * 2. GetTodoItemUseCase
     */
    @Before
    fun init() {
        initData()
    }

    // init data by mocking
    private fun initData() = runTest {
        insertToDoUseCase(mockList)
    }

    // Test: 입력된 데이터를 불러와서 검증한다.
    @Test
    fun `test viewModel fetch`(): Unit= runTest {
        // viewModel 의 리스트 Observe start
        val testObservable = viewModel.toDoListLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(mockList)
            )
        )
    }

    // Test: 데이터를 업데이트 했을 때 잘 반영되는가
    @Test
    fun `test Item Update`(): Unit = runTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        // item update
        viewModel.updateEntity(todo)
        // 정상적으로 추가되었는지 확인
        assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false == todo.hasCompleted )
    }

    // Test: 데이터를 전부 삭제했을 때 정상적으로 보여지는가
    @Test
    fun `test Item Delete All`(): Unit = runTest {
        // viewModel 의 리스트 Observe start
        val testObservable = viewModel.toDoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }
}