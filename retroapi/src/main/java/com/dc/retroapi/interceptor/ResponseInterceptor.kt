package com.dc.retroapi.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ResponseInterceptor(private val onResponseCallback: OnResponseCallback) : Interceptor {

    interface OnResponseCallback {
        fun onNetworkResponse(response: Response)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        val responseBody = originalResponse.body
        if (responseBody != null) {
            val originalResponseString = responseBody.string()
            val newResponseBody = originalResponseString.toResponseBody(responseBody.contentType())
            onResponseCallback.onNetworkResponse(originalResponse.newBuilder()
                    .body(originalResponseString.toResponseBody(responseBody.contentType())).build())
            return originalResponse.newBuilder().body(newResponseBody).build()
        } else {
            onResponseCallback.onNetworkResponse(originalResponse.newBuilder().build())
            return originalResponse.newBuilder().build()
        }
    }

}
