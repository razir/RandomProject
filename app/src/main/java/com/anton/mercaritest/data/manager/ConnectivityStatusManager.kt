package com.anton.mercaritest.data.manager

import android.content.Context
import android.net.ConnectivityManager

interface ConnectivityStatusManager {
    fun isConnected(): Boolean
}

class ConnectivityStatusManagerImpl(private val context: Context) :
    ConnectivityStatusManager {

    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}