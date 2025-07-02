package com.demo.moviehub.network

import com.demo.moviehub.util.Result

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, throwable: Throwable? = null) :
        Resource<T>(data, message, throwable)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message, throwable=$throwable]"
            is Loading<T> -> "Loading"
        }
    }
}

/**
 * Converts a [Result] to a [Resource].
 */
fun <T : Any> Result<T>.toResource(): Resource<T> {
    return when (this) {
        is Result.Success -> Resource.Success(data)
        is Result.Error -> Resource.Error(exception.message ?: "An unknown error occurred", null, exception)
        is Result.Loading -> Resource.Loading()
    }
}

/**
 * Maps the data of a [Resource] using the provided [transform] function.
 */
fun <T : Any, R : Any> Resource<T>.map(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(transform(data!!))
        is Resource.Error -> Resource.Error(
            message = message ?: "", 
            data = data?.let { transform(it) }, 
            throwable = throwable
        )
        is Resource.Loading -> Resource.Loading(data?.let { transform(it) })
    }
}

/**
 * Returns the data if the [Resource] is [Resource.Success], otherwise returns null.
 */
fun <T> Resource<T>.getDataIfSuccess(): T? {
    return if (this is Resource.Success) data else null
}

/**
 * Returns true if the [Resource] is [Resource.Loading].
 */
val Resource<*>.isLoading: Boolean
    get() = this is Resource.Loading

/**
 * Returns true if the [Resource] is [Resource.Success].
 */
val Resource<*>.isSuccess: Boolean
    get() = this is Resource.Success


/**
 * Performs the given [action] on the encapsulated data if this instance represents [Resource.Success].
 * Returns the original `Resource` unchanged.
 */
inline fun <T : Any> Resource<T>.onSuccess(action: (T) -> Unit): Resource<T> {
    if (this is Resource.Success && data != null) action(data)
    return this
}

/**
 * Performs the given [action] on the encapsulated exception if this instance represents [Resource.Error].
 * Returns the original `Resource` unchanged.
 */
inline fun <T : Any> Resource<T>.onError(action: (message: String, throwable: Throwable?) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message ?: "", throwable)
    return this
}

/**
 * Performs the given [action] if this instance represents [Resource.Loading].
 * Returns the original `Resource` unchanged.
 */
inline fun <T : Any> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Resource.Loading) action()
    return this
}
