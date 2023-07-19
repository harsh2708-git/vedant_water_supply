package com.production.vedantwatersupply.webservice

import android.content.Context
import com.dc.retroapi.BuildConfig
import com.dc.retroapi.builder.RetrofitClientBuilder
import com.dc.retroapi.interceptor.RequestInterceptor
import com.production.vedantwatersupply.app.MyApplication
import com.production.vedantwatersupply.utils.ApiConstants
import com.production.vedantwatersupply.utils.UserUtils
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
    companion object {
        val iApiEndPoints: IApiEndPoints by lazy {
            return@lazy ApiClient().getRetrofitClientBuilder()
        }
        const val BASE_URL = ApiConstants.BASE_URL
    }

    private fun getRetrofitClientBuilder(): IApiEndPoints {
        val builder = RetrofitClientBuilder()
        builder.baseUrl(BASE_URL)
        builder.connectionTimeout(30)
        builder.readTimeout(30)
        builder.writeTimeout(30)
        builder.addInterceptor(RequestInterceptor(object : RequestInterceptor.OnRequestInterceptor {
            override fun provideBodyMap(): HashMap<String, String> {
                val map = HashMap<String, String>()
//                map["ws_token"] = "28f4e69c4baec01082a6b17403499363b0e424264b5317a7cb52dd977570abe"
                return map
            }

            override fun provideHeaderMap(): HashMap<String, String> {
                val map = HashMap<String, String>()
                map["Authorization"] = "Bearer" + UserUtils.getUserData(MyApplication.context)?.token
                return map
            }

            override fun removeFromBody(): ArrayList<String> = ArrayList()
        }))
        builder.addLogInterceptor(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        builder.addConverterFactory(GsonConverterFactory.create())
        return builder.create(IApiEndPoints::class.java)
    }
}