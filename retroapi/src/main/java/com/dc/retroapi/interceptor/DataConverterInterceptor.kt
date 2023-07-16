package com.dc.retroapi.interceptor

import com.dc.retroapi.annotations.DataAsListResponse
import com.dc.retroapi.annotations.DataAsObjectResponse
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Invocation


class DataConverterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        val responseBody = originalResponse.body

        if (responseBody != null) {
            var originalResponseString = responseBody.string()

            var isObjectResponse = false
            var isListResponse = false

            request.tag(Invocation::class.java)?.let {
                isObjectResponse = it.method().getAnnotation(DataAsObjectResponse::class.java) != null
                isListResponse = it.method().getAnnotation(DataAsListResponse::class.java) != null
            }

            if (isListResponse || isObjectResponse) {
                try {
                    val jsonObject = JSONObject(originalResponseString)
                    val data = jsonObject.opt("data")
                    if (data is JSONObject) {
                        if (isListResponse) {
                            val jsonArray = JSONArray()
                            jsonArray.put(data)
                            jsonObject.put("data", jsonArray)
                        }
                    } else if (data is JSONArray) {
                        if (isObjectResponse) {
                            if (data.length() > 0) {
                                jsonObject.put("data", data.get(0))
                            } else {
                                jsonObject.remove("data")
                            }
                        }
                    }
                    originalResponseString = jsonObject.toString()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            val newResponseBody = originalResponseString.toResponseBody(responseBody.contentType())
            return originalResponse.newBuilder().body(newResponseBody).build()
        } else {
            return originalResponse.newBuilder().build()
        }
    }
}