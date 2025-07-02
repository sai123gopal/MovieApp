package com.demo.moviehub.util

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {
    object Loading : Result<Nothing>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>() {
        val message: String
            get() = exception.message ?: "An unknown error occurred"
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Result.Success] & holds non-null [Result.Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

/**
 * Returns the encapsulated value if this instance represents [Result.Success] or `null`
 * if it is [Result.Error] or [Result.Loading].
 */
fun <T : Any> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data

/**
 * Returns the encapsulated exception if this instance represents [Result.Error] or `null`
 * if it is [Result.Success] or [Result.Loading].
 */
fun <T : Any> Result<T>.exceptionOrNull(): Exception? = (this as? Result.Error)?.exception

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated data
 * if this instance represents [Result.Success], or the original [Result] if it is [Result.Error] or [Result.Loading].
 */
inline fun <T : Any, R : Any> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
        is Result.Loading -> this
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated data
 * if this instance represents [Result.Success], or the original [Result] if it is [Result.Error] or [Result.Loading].
 */
suspend fun <T : Any, R : Any> Result<T>.suspendMap(transform: suspend (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
        is Result.Loading -> this
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated data
 * if this instance represents [Result.Success], or the original [Result] if it is [Result.Error] or [Result.Loading].
 */
inline fun <T : Any, R : Any> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> {
    return when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
        is Result.Loading -> this
    }
}

/**
 * Performs the given [action] on the encapsulated data if this instance represents [Result.Success].
 * Returns the original `Result` unchanged.
 */
inline fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * Performs the given [action] on the encapsulated exception if this instance represents [Result.Error].
 * Returns the original `Result` unchanged.
 */
inline fun <T : Any> Result<T>.onError(action: (Exception) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

/**
 * Performs the given [action] if this instance represents [Result.Loading].
 * Returns the original `Result` unchanged.
 */
inline fun <T : Any> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}
