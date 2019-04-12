package com.anton.mercaritest

import android.app.Application
import com.anton.mercaritest.di.appModule
import com.anton.mercaritest.di.networkModule
import org.koin.android.ext.android.startKoin

class MercariApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin(this, modules = listOf(networkModule, appModule))
    }
}