package com.sambudisp.muvi

import android.app.Application
import com.google.gson.GsonBuilder
import com.sambudisp.muvi.network.ApiServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MuviApp : Application() {

    companion object {
        lateinit var apiService: ApiServices
    }

    override fun onCreate() {
        super.onCreate()
        setupRetrofit()
    }

    private fun setupRetrofit() {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getOkHttpClient())
            .build()
        apiService = retrofit.create(ApiServices::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val timeOut: Long = 15
        return OkHttpClient.Builder()
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
    }
}