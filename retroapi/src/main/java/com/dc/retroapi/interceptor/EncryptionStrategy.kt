package com.dc.retroapi.interceptor

enum class EncryptionStrategy {
    NONE,
    REQUEST,
    RESPONSE,
    REQUEST_RESPONSE,


}

fun EncryptionStrategy.isRequireOnRequest(): Boolean {
    return this == EncryptionStrategy.REQUEST || this== EncryptionStrategy.REQUEST_RESPONSE
}

fun EncryptionStrategy.isRequireOnResponse(): Boolean {
    return this == EncryptionStrategy.RESPONSE || this== EncryptionStrategy.REQUEST_RESPONSE
}