package com.pandorina.uzman_ogretmenlik.core

import retrofit2.Response

abstract class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>):
            Result<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful){
                val body = response.body()
                body?.let {
                    return Result.success(body)
                }
            }
            return Result.failure(UnknownError( "${response.code()} ${response.message()}"))
        } catch (e: Exception){
            return Result.failure(e)
        }
    }
}