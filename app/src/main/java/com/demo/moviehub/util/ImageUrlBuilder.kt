package com.demo.moviehub.util

import com.demo.moviehub.data.model.Movie


object ImageUrlBuilder {
    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    
    const val IMAGE_SIZE_W92 = "w92"
    const val IMAGE_SIZE_W154 = "w154"
    const val IMAGE_SIZE_W185 = "w185"
    const val IMAGE_SIZE_W342 = "w342"
    const val IMAGE_SIZE_W500 = "w500"
    const val IMAGE_SIZE_W780 = "w780"
    const val IMAGE_SIZE_ORIGINAL = "original"
    
    private const val DEFAULT_POSTER_URL = "https://via.placeholder.com/500x750?text=No+Poster"
    private const val DEFAULT_BACKDROP_URL = "https://via.placeholder.com/1280x720?text=No+Backdrop"

    fun buildPosterUrl(movie: Movie, size: String = IMAGE_SIZE_W500): String {
        return movie.posterPath?.takeIf { it.isNotBlank() }?.let { path ->
            // Ensure the path starts with a forward slash
            val cleanPath = if (path.startsWith('/')) path else "/$path"
            "${BASE_IMAGE_URL}${size}${cleanPath}"
        } ?: DEFAULT_POSTER_URL
    }


    fun buildBackdropUrl(movie: Movie, size: String = IMAGE_SIZE_ORIGINAL): String {
        return movie.backdropPath?.takeIf { it.isNotBlank() }?.let { path ->
            // Ensure the path starts with a forward slash
            val cleanPath = if (path.startsWith('/')) path else "/$path"
            "${BASE_IMAGE_URL}${size}${cleanPath}"
        } ?: DEFAULT_BACKDROP_URL
    }
    

    fun buildProfileUrl(path: String?, size: String = IMAGE_SIZE_W185): String {
        return path?.takeIf { it.isNotBlank() }?.let { 
            val cleanPath = if (it.startsWith('/')) it else "/$it"
            "${BASE_IMAGE_URL}${size}${cleanPath}"
        } ?: DEFAULT_POSTER_URL
    }
}
