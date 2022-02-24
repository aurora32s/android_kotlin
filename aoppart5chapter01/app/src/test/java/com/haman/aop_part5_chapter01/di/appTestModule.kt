package com.haman.aop_part5_chapter01.di

import com.haman.aop_part5_chapter01.data.repository.TestToDoRepository
import com.haman.aop_part5_chapter01.data.repository.ToDoRepository
import com.haman.aop_part5_chapter01.domain.todo.*
import com.haman.aop_part5_chapter01.presentation.detail.DetailMode
import com.haman.aop_part5_chapter01.presentation.detail.DetailViewModel
import com.haman.aop_part5_chapter01.presentation.list.ListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {
    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            getToDoItemUseCase = get(),
            deleteToDoItemUseCase = get(),
            updateToDoUseCase = get(),
            insertToDoItemUseCase = get()
        )
    }
    // UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }
    // Repository
    single<ToDoRepository> { TestToDoRepository() }
}