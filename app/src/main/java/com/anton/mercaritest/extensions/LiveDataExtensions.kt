package com.anton.mercaritest.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.anton.mercaritest.utils.SingleLiveEvent

class NonNullMediatorLiveData<T> : MediatorLiveData<T>()

inline fun <reified T> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    if (T::class == Unit::class && this is SingleLiveEvent) {
        throw IllegalArgumentException("Can't use nonNull with SingleLiveEvent<Unit>")
    }
    val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
    mediator.addSource(this, Observer { it?.let { mediator.value = it } })
    return mediator
}

fun <T> NonNullMediatorLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}