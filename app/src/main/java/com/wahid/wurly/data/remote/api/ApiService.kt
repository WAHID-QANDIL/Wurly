package com.wahid.wurly.data.remote.api

import com.wahid.wurly.utils.ApiResult
import retrofit2.Response
import java.io.IOException

interface ApiService

suspend fun <T : Any> ApiService.safeApiCall(
    apiCall: suspend () -> Response<T>
): ApiResult<T> {
    return try {

        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(data = body)
            } else {
                throw IllegalStateException("Response body is null")
            }
        } else {
            ApiResult.HttpError(
                code = response.code(),
                message = response.message()
            )
        }
    } catch (e: Exception) {
        throw e
    }
}