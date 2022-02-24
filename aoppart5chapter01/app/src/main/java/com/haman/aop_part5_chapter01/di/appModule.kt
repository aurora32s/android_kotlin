package com.haman.aop_part5_chapter01.di

import android.content.Context
import androidx.room.Room
import com.haman.aop_part5_chapter01.data.local.db.ToDoDatabase
import com.haman.aop_part5_chapter01.data.repository.DefaultToDoRepository
import com.haman.aop_part5_chapter01.data.repository.ToDoRepository
import com.haman.aop_part5_chapter01.domain.todo.*
import com.haman.aop_part5_chapter01.domain.todo.GetToDoListUseCase
import com.haman.aop_part5_chapter01.domain.todo.InsertToDoListUseCase
import com.haman.aop_part5_chapter01.domain.todo.UpdateToDoUseCase
import com.haman.aop_part5_chapter01.presentation.detail.DetailMode
import com.haman.aop_part5_chapter01.presentation.detail.DetailViewModel
import com.haman.aop_part5_chapter01.presentation.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

// package 내에서만 사용
internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // Repository
    single { provideToDoDao(get()) }
    single { provideDB(androidApplication()) }
    single<ToDoRepository> { DefaultToDoRepository(get(),get()) }
    // UseCase
    factory { GetToDoListUseCase(get()) }
    factory { InsertToDoListUseCase(get()) }
    factory { InsertToDoItemUseCase(get()) }
    factory { UpdateToDoUseCase(get()) }
    factory { GetToDoItemUseCase(get()) }
    factory { DeleteAllToDoUseCase(get()) }
    factory { DeleteToDoItemUseCase(get()) }
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
}

internal fun provideDB(context: Context): ToDoDatabase =
    Room.databaseBuilder(context, ToDoDatabase::class.java, ToDoDatabase.DB_NAME).build()
internal fun provideToDoDao(database: ToDoDatabase) = database.toDoDao()