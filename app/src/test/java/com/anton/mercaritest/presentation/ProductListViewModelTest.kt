package com.anton.mercaritest.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.data.manager.ConnectivityStatusManager
import com.anton.mercaritest.data.usecase.GetProductsUseCase
import com.anton.mercaritest.presentation.timeline.product.ProductsListViewModel
import com.anton.mercaritest.util.testObserver
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ProductListViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    val getProductsUseCase: GetProductsUseCase = mock()
    val connectivityStatusManager: ConnectivityStatusManager = mock()
    val category = Category()
    lateinit var viewModel: ProductsListViewModel

    @Before
    fun setup() {
        viewModel = ProductsListViewModel(getProductsUseCase, connectivityStatusManager, category)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun reset() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun handleLoadIfNeeded_noData_success() {
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(getProductsUseCase).execute(any(), any())

        val loadingObserver = viewModel.showProductsLoading.testObserver()
        val productsObserver = viewModel.products.testObserver()
        val errorAlertObserver = viewModel.errorAlert.testObserver()
        val errorStateObserver = viewModel.errorState.testObserver()

        viewModel.handleLoadIfNeeded()

        assertEquals(listOf(null), errorStateObserver.observedValues)
        assertTrue(errorAlertObserver.observedValues.isEmpty())
        assertEquals(listOf(true, false), loadingObserver.observedValues)
        assertEquals(listOf(products), productsObserver.observedValues)

    }

    @Test
    fun handleLoadIfNeeded_noData_fail() {
        doReturn(Single.error<List<Product>>(IOException())).whenever(getProductsUseCase).execute(any(), any())

        val loadingObserver = viewModel.showProductsLoading.testObserver()
        val productsObserver = viewModel.products.testObserver()
        val errorAlertObserver = viewModel.errorAlert.testObserver()
        val errorStateObserver = viewModel.errorState.testObserver()

        viewModel.handleLoadIfNeeded()

        assertEquals(null, errorStateObserver.observedValues[0])
        assertEquals(2, errorStateObserver.observedValues.size)
        assertEquals(listOf(true, false), loadingObserver.observedValues)
        assertTrue(productsObserver.observedValues.isEmpty())
        assertTrue(errorAlertObserver.observedValues.isEmpty())
    }

    @Test
    fun handleLoadIfNeeded_hasData() {
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(getProductsUseCase).execute(any(), any())
        val productsObserver = viewModel.products.testObserver()
        viewModel.handleRefreshProducts()

        val products2 = listOf(Product(), Product())
        doReturn(Single.just(products2)).whenever(getProductsUseCase).execute(any(), any())
        viewModel.handleLoadIfNeeded()

        assertEquals(listOf(products), productsObserver.observedValues)
    }

    @Test
    fun handleRefreshError_noConnection() {
        doReturn(false).whenever(connectivityStatusManager).isConnected()
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(getProductsUseCase).execute(any(), any())

        val loadingObserver = viewModel.showProductsLoading.testObserver()
        val productsObserver = viewModel.products.testObserver()

        viewModel.handleRefreshError()

        assertTrue(loadingObserver.observedValues.isEmpty())
        assertTrue(productsObserver.observedValues.isEmpty())
    }

    @Test
    fun handleRefreshError_hasConnection_success() {
        doReturn(true).whenever(connectivityStatusManager).isConnected()
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(getProductsUseCase).execute(any(), any())

        val loadingObserver = viewModel.showProductsLoading.testObserver()
        val productsObserver = viewModel.products.testObserver()
        val errorAlertObserver = viewModel.errorAlert.testObserver()
        val errorStateObserver = viewModel.errorState.testObserver()

        viewModel.handleRefreshError()

        assertEquals(listOf(null), errorStateObserver.observedValues)
        assertTrue(errorAlertObserver.observedValues.isEmpty())
        assertEquals(listOf(true, false), loadingObserver.observedValues)
        assertEquals(listOf(products), productsObserver.observedValues)
    }

    @Test
    fun handleRefreshProducts_hasData_success() {
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(getProductsUseCase).execute(any(), any())

        val hideSwipeRefreshObserver = viewModel.hideSwipeRefresh.testObserver()
        val productsObserver = viewModel.products.testObserver()
        val errorAlertObserver = viewModel.errorAlert.testObserver()
        val errorStateObserver = viewModel.errorState.testObserver()

        viewModel.handleRefreshProducts()

        val products2 = listOf(Product(), Product())
        doReturn(Single.just(products2)).whenever(getProductsUseCase).execute(any(), any())

        viewModel.handleRefreshProducts()

        assertEquals(listOf(null, null), errorStateObserver.observedValues)
        assertTrue(errorAlertObserver.observedValues.isEmpty())
        assertEquals(2, hideSwipeRefreshObserver.observedValues.size)
        assertEquals(listOf(products, products2), productsObserver.observedValues)
    }

    @Test
    fun handleRefreshProducts_hasData_fail() {
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(getProductsUseCase).execute(any(), any())

        val hideSwipeRefreshObserver = viewModel.hideSwipeRefresh.testObserver()
        val productsObserver = viewModel.products.testObserver()
        val errorAlertObserver = viewModel.errorAlert.testObserver()
        val errorStateObserver = viewModel.errorState.testObserver()
        val showLoadingObserver = viewModel.showProductsLoading.testObserver()

        viewModel.handleRefreshProducts()

        doReturn(Single.error<List<Product>>(IOException())).whenever(getProductsUseCase).execute(any(), any())

        viewModel.handleRefreshProducts()

        assertEquals(listOf(null, null), errorStateObserver.observedValues)
        assertEquals(listOf(true, false, false), showLoadingObserver.observedValues)
        assertEquals(1, errorAlertObserver.observedValues.size)
        assertEquals(2, hideSwipeRefreshObserver.observedValues.size)
        assertEquals(listOf(products), productsObserver.observedValues)
    }
}