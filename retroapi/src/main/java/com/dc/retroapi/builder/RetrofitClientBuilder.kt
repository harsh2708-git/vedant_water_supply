package com.dc.retroapi.builder


import com.dc.retroapi.interceptor.EncryptionStrategy
import com.dc.retroapi.interceptor.ResponseInterceptor
import com.dc.retroapi.interceptor.SecureRequestInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by HB on 28/8/19.
 */

class RetrofitClientBuilder {

    private val client = OkHttpClient.Builder()
    private val retrofit = Retrofit.Builder()

    fun baseUrl(url: String): RetrofitClientBuilder {
        retrofit.baseUrl(url)
        return this
    }

    fun addLogInterceptor(logLevel: HttpLoggingInterceptor.Level, logger: HttpLoggingInterceptor.Logger? = null): RetrofitClientBuilder {
        val loggingInterceptor = if (logger != null) HttpLoggingInterceptor(logger) else HttpLoggingInterceptor()
        loggingInterceptor.level = logLevel
        client.addInterceptor(loggingInterceptor)
        return this
    }


    fun addEncryptionInterceptor(encryptionKey: String, encryptionStrategy: EncryptionStrategy,
                                 enableChecksum: Boolean, checksumKey: String,
                                 checksumBcrypt:Boolean,
                                 excludeFromChecksum: ArrayList<String>? = null,
                                 excludeFromEncryption: ArrayList<String>? = null): RetrofitClientBuilder {
        client.addInterceptor(SecureRequestInterceptor(encryptionKey, encryptionStrategy, enableChecksum, checksumKey,checksumBcrypt,
                excludeFromChecksum, excludeFromEncryption))
        return this
    }

    fun addResponseInterceptor(callback: ResponseInterceptor.OnResponseCallback) : RetrofitClientBuilder {
        client.addInterceptor(ResponseInterceptor(callback))
        return this
    }

    fun addConverterFactory(factory: Converter.Factory): RetrofitClientBuilder {
        retrofit.addConverterFactory(factory)
        return this
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory): RetrofitClientBuilder {
        retrofit.addCallAdapterFactory(factory)
        return this
    }

    fun connectionTimeout(timeInSecond: Int): RetrofitClientBuilder {
        client.connectTimeout(timeInSecond.toLong(), TimeUnit.SECONDS)
        return this
    }

    fun readTimeout(timeInSecond: Int): RetrofitClientBuilder {
        client.readTimeout(timeInSecond.toLong(), TimeUnit.SECONDS)
        return this
    }


    fun writeTimeout(timeInSecond: Int): RetrofitClientBuilder {
        client.writeTimeout(timeInSecond.toLong(), TimeUnit.SECONDS)
        return this
    }


    fun <T> create(service: Class<T>): T {
        return retrofit.client(client.build()).build().create(service)
    }

    fun addInterceptor(interceptor: Interceptor): RetrofitClientBuilder {
        client.addInterceptor(interceptor)
        return this
    }
}
