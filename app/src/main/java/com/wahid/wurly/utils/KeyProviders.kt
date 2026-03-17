package com.wahid.wurly.utils


/**
 * This keyProvider is for providing keys and tokens in binary representation from C++ source
 * */
object KeyProvider {
    init {
        System.loadLibrary("keys")
    }
    external fun getAppId(): String
    external fun getBaseUrl(): String
}