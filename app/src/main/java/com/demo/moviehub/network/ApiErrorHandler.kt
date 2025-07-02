package com.demo.moviehub.network

import android.content.Context
import com.demo.moviehub.R
import com.demo.moviehub.util.Result
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiErrorHandler @Inject constructor(
    private val context: Context
) {

    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    400 -> context.getString(R.string.error_bad_request)
                    401 -> context.getString(R.string.error_unauthorized)
                    403 -> context.getString(R.string.error_forbidden)
                    404 -> context.getString(R.string.error_not_found)
                    408 -> context.getString(R.string.error_request_timeout)
                    500 -> context.getString(R.string.error_internal_server)
                    502 -> context.getString(R.string.error_bad_gateway)
                    503 -> context.getString(R.string.error_service_unavailable)
                    504 -> context.getString(R.string.error_gateway_timeout)
                    else -> context.getString(R.string.error_unknown, throwable.code())
                }
            }
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.no_internet_connection)
            is IOException -> context.getString(R.string.error_network)
            else -> context.getString(R.string.error_unknown, throwable.message ?: "")
        }
    }

    fun getErrorMessage(result: Result.Error): String? {
        return getErrorMessage(result.exception)
    }

    fun getErrorMessage(resource: Resource.Error<*>): String? {
        return resource.message ?: resource.throwable?.let { getErrorMessage(it) }
    }

    companion object {
        fun createError(context: Context, throwable: Throwable): Result.Error {
            val errorMessage = ApiErrorHandler(context).getErrorMessage(throwable)
            return Result.Error(IOException(errorMessage, throwable))
        }

        fun <T> createResourceError(
            context: Context,
            throwable: Throwable,
            data: T? = null
        ): Resource.Error<T> {
            val errorMessage = ApiErrorHandler(context).getErrorMessage(throwable)
            return Resource.Error(errorMessage, data, throwable)
        }
    }
}
