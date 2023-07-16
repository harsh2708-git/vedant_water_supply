package com.dc.retroapi.interceptor

import android.text.TextUtils
import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.dc.retroapi.annotations.RequestExclusionStrategy
import com.dc.retroapi.utils.AESEncrypter
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import retrofit2.Invocation
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


/**
 * Created by HB on 21/8/19.

Interceptor used for following things
- Encryption of request parameter - supports GET,POST,DELETE,PUT,PATCH http methods
- Generate checksum based on request parameter and attach to header/body
- Authentication - To send authorization in header and if request is unauthorized then it will retry with refresh token with {n} times

 */


/**  Things to do
 *  Json Support check
 *  Path Support*/

class SecureRequestInterceptor(private val encryptionKey: String = "",
                               private val encryptionStrategy: EncryptionStrategy = EncryptionStrategy.NONE,
                               private val enableChecksumForAllRequest: Boolean = false,
                               private val checksumKey: String = "",
                               private val checksumBcrypt: Boolean = false,
                               private val excludeFromChecksum: ArrayList<String>? = null,
                               private val excludeFromEncryption: ArrayList<String>? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var checksumEnable = this@SecureRequestInterceptor.enableChecksumForAllRequest
        var encryptionStrategy = this@SecureRequestInterceptor.encryptionStrategy

        var request = chain.request()
        val method = request.method

        /** if particular api doesn't want encryption and/or checksum it can achieve by
        see [RequestExclusionStrategy] */

        request.tag(Invocation::class.java)?.let {
            it.method().getAnnotation(RequestExclusionStrategy::class.java)?.let { tag ->
                val tagValues = TextUtils.split(tag.value, ",").asList()
                if (tagValues.contains("checksum")) {
                    checksumEnable = false
                }
                if (tagValues.contains("encryption")) {
                    encryptionStrategy = EncryptionStrategy.NONE
                }
            }
        }


        //Encrypion checksum logic starts here
        if (encryptionStrategy != EncryptionStrategy.NONE || checksumEnable) {
            if (method == "GET") {

                // If Request method is GET
                val fullUrl = request.url
                val sortedTextValueMap = TreeMap<String, String>() // to put all text parameter key/value
                val parameterNames = fullUrl.queryParameterNames

                val parameterIterator = parameterNames.iterator()
                val urlRequestBuilder = request.url.newBuilder()

                while (parameterIterator.hasNext()) {
                    val key = parameterIterator.next()
                    val values = fullUrl.queryParameterValues(key)
                    val value = fullUrl.queryParameterValues(key)[values.lastIndex]//If query has multiple value then dev needs to send in other form
                    if (!isExcludeFromChecksum(key)) {
                        sortedTextValueMap[key] = value!!
                    }
                    urlRequestBuilder.removeAllQueryParameters(key) // remove already query parameter
                    urlRequestBuilder.addQueryParameter(key, encryptIfRequired(key, value!!, encryptionStrategy.isRequireOnRequest()))
                }

                if (checksumEnable) {
                    urlRequestBuilder.addQueryParameter(checksumKey, generateChecksum(sortedTextValueMap))
                }
                request = request.newBuilder().get().url(urlRequestBuilder.build()).build()

            } else {
                // If Request method is POST / DELETE /  PUT / PATCH ..
                val requestBody = request.body
                if (requestBody != null) {
                    /*val subType = requestBody.contentType()?.subtype
                    log("Request Body type", subType)*/

                    lateinit var newBody: RequestBody
                    when {
                        request.body is FormBody -> { // if request body is FORM BODY

                            val formBody = request.body as FormBody
                            val size = formBody.size
                            val newBodyBuilder = FormBody.Builder()

                            val sortedTextValueMap = TreeMap<String, String>() // to put all text parameter key/value
                            for (i in 0 until size) {
                                val key = formBody.name(i)
                                val value = formBody.value(i)
                                if (!isExcludeFromChecksum(key)) {
                                    sortedTextValueMap[key] = value
                                }
                                newBodyBuilder.add(key, encryptIfRequired(key, value, encryptionStrategy.isRequireOnRequest()))
                            }

                            if (checksumEnable) {
                                newBodyBuilder.add(checksumKey, generateChecksum(sortedTextValueMap))
                            }
                            newBody = newBodyBuilder.build()
                        }
                        request.body is MultipartBody -> { //if request body is MULTIPART BODY
                            val formBody = request.body as MultipartBody
                            val partList = formBody.parts

                            val newBodyBuilder = MultipartBody.Builder()
                            newBodyBuilder.setType(MultipartBody.FORM)

                            val sortedTextValueMap = TreeMap<String, String>() // to put all text parameter key/value
                            for (i in partList) {
                                if (i.body.contentType()?.type?.equals("text")!!) { //"text", "image", "audio", "video"
                                    val key = getKeyFromContentDisposition(i.headers?.get("Content-Disposition")!!)
                                    val value = bodyToString(i.body)
                                    if (!isExcludeFromChecksum(key)) {
                                        sortedTextValueMap[key] = value
                                    }

                                    newBodyBuilder.addPart(MultipartBody.Part.createFormData(key,
                                            encryptIfRequired(key, value, encryptionStrategy.isRequireOnRequest())))
                                } else {
                                    val key = getKeyFromContentDisposition(i.headers?.get("Content-Disposition")!!)
                                    if (!isExcludeFromChecksum(key)) {
                                        sortedTextValueMap[key] = ""
                                    }
                                    newBodyBuilder.addPart(i.headers, i.body)
                                }
                            }

                            if (checksumEnable) {
                                newBodyBuilder.addFormDataPart(checksumKey, generateChecksum(sortedTextValueMap))
                            }
                            newBody = newBodyBuilder.build()
                        }
                        else -> newBody = requestBody
                    }


                    //Check what method type user requested and pass parameters to respective method
                    lateinit var requestBuilder: Request.Builder
                    requestBuilder = when (request.method) {
                        "POST" -> request.newBuilder().post(newBody)
                        "PUT" -> request.newBuilder().put(newBody)
                        "PATCH" -> request.newBuilder().patch(newBody)
                        "DELETE" -> request.newBuilder().delete(newBody)
                        else -> request.newBuilder().post(newBody)
                    }
                    request = requestBuilder.build()
                }

            }
        }

        // decrypt encrypted response
        //Response Handling
        val originalResponse = chain.proceed(request)
        val responseBody = originalResponse.body
        if (responseBody != null) {
            val originalResponseString = responseBody.string()
            val finalStringResponse = decryptIfRequired(originalResponseString, encryptionStrategy.isRequireOnResponse())
            val newResponseBody = finalStringResponse.toResponseBody(responseBody.contentType())
            return originalResponse.newBuilder().body(newResponseBody).build()
        } else {
            return originalResponse.newBuilder().build()
        }
    }


    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }

    private fun getKeyFromContentDisposition(value: String): String {
        val pat = Pattern.compile("(?<=name=\")\\w+")
        val mat = pat.matcher(value)
        while (mat.find()) {
            return (mat.group())
        }
        return ""
    }


    private fun encryptIfRequired(key: String, value: String, encryptionEnable: Boolean): String {
        val aesEncrypt: AESEncrypter = AESEncrypter(encryptionKey)
        return if (encryptionEnable && !isExcludeFromEncryption(key)) aesEncrypt.encrypt(value) else value
    }

    private fun decryptIfRequired(value: String, encryptionEnable: Boolean): String {
        val aesEncrypt: AESEncrypter = AESEncrypter(encryptionKey)
        try {
            return if (encryptionEnable) aesEncrypt.decrypt(value) else value
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return value
    }

    private fun isExcludeFromChecksum(key: String): Boolean {
        return excludeFromChecksum?.contains(key) ?: false
    }

    private fun isExcludeFromEncryption(key: String): Boolean {
        return excludeFromEncryption?.contains(key) ?: false
    }

    private fun generateChecksum(map: TreeMap<String, String>): String {
        val aesEncrypt: AESEncrypter = AESEncrypter(encryptionKey)
        val checkSum = aesEncrypt.generateChecksumFromSortedMap(map)
        if (checksumBcrypt) {
            return BCrypt.withDefaults().hashToString(5, checkSum.toCharArray())
        }
        return checkSum
    }

    private fun log(tag: String, message: String?) {
        Log.e(tag, message!!)
    }
}
