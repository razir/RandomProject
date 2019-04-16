package com.anton.mercaritest.presentation.timeline.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.data.manager.ConnectivityStatusManager
import com.anton.mercaritest.data.usecase.GetProductsUseCase
import com.anton.mercaritest.extensions.addTo
import com.anton.mercaritest.extensions.toErrorData
import com.anton.mercaritest.presentation.error.ErrorData
import com.anton.mercaritest.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductsListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val connectivityStatusManager: ConnectivityStatusManager,
    private val category: Category
) : ViewModel() {

    val products = MutableLiveData<List<Product>>()
    val showProductsLoading = MutableLiveData<Boolean>()
    val hideSwipeRefresh = SingleLiveEvent<Unit>()
    val errorAlert = SingleLiveEvent<ErrorData>()
    val errorState = MutableLiveData<ErrorData>()

    private val compositeDisposable = CompositeDisposable()

    fun handleRefreshProducts() {
        loadProducts(forceRefresh = true)
    }

    fun handleRefreshError() {
        if (connectivityStatusManager.isConnected()) {
            loadProducts()
        }
    }

    fun handleLoadIfNeeded() {
        if (!hasProductsData()) {
            loadProducts()
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun loadProducts(forceRefresh: Boolean = false) {
        if (!hasProductsData()) {
            showProductsLoading.value = true
        }
        errorState.value = null
        getProductsUseCase.execute(category, forceRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showProductsLoading.value = false
                hideSwipeRefresh.call()
                products.value = it
            }, {
                showProductsLoading.value = false
                hideSwipeRefresh.call()
                val errorData = it.toErrorData()
                if (hasProductsData()) {
                    errorAlert.value = errorData
                } else {
                    errorState.value = errorData
                }
            }).addTo(compositeDisposable)
    }

    private fun hasProductsData() = products.value != null

}