package com.anton.mercaritest.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.manager.ConnectivityStatusManager
import com.anton.mercaritest.data.usecase.CleanProductsCacheUseCase
import com.anton.mercaritest.data.usecase.GetCategoriesUseCase
import com.anton.mercaritest.presentation.timeline.TimelineViewModel
import com.anton.mercaritest.util.testObserver
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class TimelineViewModelTest {
    val getCategoriesUseCase: GetCategoriesUseCase = mock()
    var cleanProductsCacheUseCase: CleanProductsCacheUseCase = mock()
    val connectivityManager: ConnectivityStatusManager = mock()
    lateinit var viewModel: TimelineViewModel
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
    }

    @After
    fun reset() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun init_loadCategories_fail() {
        doReturn(Completable.complete()).whenever(cleanProductsCacheUseCase).execute()
        doReturn(Single.error<List<Category>>(IOException())).whenever(getCategoriesUseCase).execute()

        initViewModel()

        val errorStateObserver = viewModel.errorState.testObserver()
        val loadingObserver = viewModel.showCategoriesLoading.testObserver()
        val categoriesObserver = viewModel.categories.testObserver()


        testScheduler.triggerActions()
        assertEquals(1, errorStateObserver.observedValues.size)
        assertEquals(listOf(true, false), loadingObserver.observedValues)
        assertTrue(categoriesObserver.observedValues.isEmpty())
    }

    @Test
    fun init_loadCategories_success() {
        val categoriesList = listOf(Category(), Category())
        doReturn(Completable.complete()).whenever(cleanProductsCacheUseCase).execute()
        doReturn(Single.just(categoriesList)).whenever(getCategoriesUseCase).execute()

        initViewModel()

        val errorStateObserver = viewModel.errorState.testObserver()
        val loadingObserver = viewModel.showCategoriesLoading.testObserver()
        val categoriesObserver = viewModel.categories.testObserver()


        testScheduler.triggerActions()
        assertTrue(errorStateObserver.observedValues.isEmpty())
        assertEquals(listOf(true, false), loadingObserver.observedValues)
        assertEquals(listOf(categoriesList), categoriesObserver.observedValues)
    }

    @Test
    fun refresh_afterError_noConnection() {
        doReturn(Completable.never()).whenever(cleanProductsCacheUseCase).execute()
        doReturn(false).whenever(connectivityManager).isConnected()

        initViewModel()

        val errorStateObserver = viewModel.errorState.testObserver()
        val loadingObserver = viewModel.showCategoriesLoading.testObserver()

        viewModel.handleErrorRefresh()

        testScheduler.triggerActions()

        assertTrue(errorStateObserver.observedValues.isEmpty())
        assertTrue(loadingObserver.observedValues.isEmpty())
    }

    @Test
    fun refresh_afterError_hasConnection() {
        doReturn(true).whenever(connectivityManager).isConnected()
        doReturn(Completable.never()).whenever(cleanProductsCacheUseCase).execute()

        initViewModel()

        val errorStateObserver = viewModel.errorState.testObserver()
        val loadingObserver = viewModel.showCategoriesLoading.testObserver()
        val categoriesObserver = viewModel.categories.testObserver()

        doReturn(Completable.complete()).whenever(cleanProductsCacheUseCase).execute()
        val categoriesList = listOf(Category(), Category())
        doReturn(Single.just(categoriesList)).whenever(getCategoriesUseCase).execute()

        viewModel.handleErrorRefresh()
        testScheduler.triggerActions()

        assertEquals(listOf(null), errorStateObserver.observedValues)
        assertEquals(listOf(true, false), loadingObserver.observedValues)
        assertEquals(listOf(categoriesList), categoriesObserver.observedValues)
    }

    private fun initViewModel() {
        viewModel = TimelineViewModel(getCategoriesUseCase, cleanProductsCacheUseCase, connectivityManager)
    }
}