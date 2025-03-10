package com.wus.loan.remote

import android.content.Context
import com.ards.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    /*fun getCasheService(context: Context): CasheService =
        buildRetrofitClient(context).create(CasheService::class.java)*/
    private fun buildRetrofitClient(context: Context): Retrofit {
        val okHttpClient =
            getOkHttpClient(context,makeHttpLoggingInterceptor())
        return makeErosNowApiService(
            okHttpClient
        )
    }
    private fun makeErosNowApiService(okHttpClient: OkHttpClient):
            Retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getOkHttpClient(
        context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        return okHttpClientBuilder
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

    }
    private fun makeHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    object RetrofitClient {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constant.BASE_URL) // Use the BASE_URL from Constants.kt
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <T> createService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }


    }

    object RetrofitClients {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constant.BASE_URLA) // Use the BASE_URL from Constants.kt
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <T> createService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }


    }



}