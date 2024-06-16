package com.github.test.application.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.github.test.application.domain.repository.WebViewRepository

class WebViewRepositoryImpl(private val context: Context): WebViewRepository {
    override fun getInternetStatus(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNet = connectivityManager.activeNetwork
        if (activeNet != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNet)
            return networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        return false
    }
}