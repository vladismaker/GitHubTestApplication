package com.github.test.application.data.api

import com.github.test.application.data.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {

    private var INSTANCE: Retrofit? = null

    fun getInstance(): Retrofit {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().also {
                    INSTANCE = it
                }
        }
    }
}
