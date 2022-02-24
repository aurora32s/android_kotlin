package com.haman.aop_part5_chapter01.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.haman.aop_part5_chapter01.di.appTestModule
import com.haman.aop_part5_chapter01.livedata.LiveDataTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@ExperimentalCoroutinesApi
internal abstract class ViewModelTest: KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var context: Application

    // coroutine test -> change thread easily
    private val dispatcher = TestCoroutineDispatcher()

    // test pre
    @Before
    fun setup() {
        startKoin {
            androidContext(context)
            modules(appTestModule) // DI
        }
        Dispatchers.setMain(dispatcher)
    }

    // test post
    @After
    fun tearDown() {
        // clear memory started at Before
        stopKoin()
        // MainDispatcher 를 초기화 해주어야 메모리 누수가 발생하지 않음.
        Dispatchers.resetMain()
    }

    protected fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {
        val testObserver = LiveDataTestObserver<T>()
        // TODO 테스트 완료 후 해제 필요
        observeForever(testObserver)
        return testObserver
    }
}