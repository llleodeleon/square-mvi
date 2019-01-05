package com.leodeleon.square.data

import com.leodeleon.square.utils.TOKEN_PLACEHOLDER
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkInterceptor(private val token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if(token != TOKEN_PLACEHOLDER){
            chain.request().newBuilder().header("Authorization", "token $token").build()
        } else chain.request()
        return chain.proceed(request)
    }
}