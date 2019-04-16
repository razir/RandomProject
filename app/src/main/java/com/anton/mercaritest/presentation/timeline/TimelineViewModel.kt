package com.anton.mercaritest.presentation.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.manager.ConnectivityStatusManager
import com.anton.mercaritest.data.usecase.CleanProductsCacheUseCase
import com.anton.mercaritest.data.usecase.GetCategoriesUseCase
import com.anton.mercaritest.extensions.addTo
import com.anton.mercaritest.extensions.toErrorData
import com.anton.mercaritest.presentation.error.ErrorData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TimelineViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val cleanProductsCacheUseCase: CleanProductsCacheUseCase,
    private val connectivityStatusManager: ConnectivityStatusManager
) : ViewModel() {

    val categories = MutableLiveData<List<Category>>()
    val errorState = MutableLiveData<ErrorData>()
    val showCategoriesLoading = MutableLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    init {
        invalidateCacheAndLoad()
    }

    fun handleErrorRefresh() {
        if (connectivityStatusManager.isConnected()) {
            errorState.value = null
            loadCategories()
        }
    }

    private fun invalidateCacheAndLoad() {
        cleanProductsCacheUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete()
            .subscribe {
                loadCategories()
            }
            .addTo(compositeDisposable)
    }

    private fun loadCategories() {
        showCategoriesLoading.value = true
        getCategoriesUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showCategoriesLoading.value = false
                categories.value = it

            }, {
                showCategoriesLoading.value = false
                errorState.value = it.toErrorData()
            })
            .addTo(compositeDisposable)

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}