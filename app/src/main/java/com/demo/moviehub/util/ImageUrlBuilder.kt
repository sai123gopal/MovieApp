package com.demo.moviehub.util

import com.demo.moviehub.R
import com.demo.moviehub.data.model.Movie

/**
 * Helper object for building image URLs for TMDB API
 */
object ImageUrlBuilder {
    // Base URL for all TMDB images
    private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    
    // Image sizes
    const val IMAGE_SIZE_W92 = "w92"
    const val IMAGE_SIZE_W154 = "w154"
    const val IMAGE_SIZE_W185 = "w185"
    const val IMAGE_SIZE_W342 = "w342"
    const val IMAGE_SIZE_W500 = "w500"
    const val IMAGE_SIZE_W780 = "w780"
    const val IMAGE_SIZE_ORIGINAL = "original"
    
    // Default placeholder image URLs
    private const val DEFAULT_POSTER_URL = "https://via.placeholder.com/500x750?text=No+Poster"
    private const val DEFAULT_BACKDROP_URL = "https://via.placeholder.com/1280x720?text=No+Backdrop"

    /**
     * Builds a URL for a movie poster image
     * @param movie The movie containing the poster path
     * @param size The desired image size (defaults to w500)
     * @return Complete URL for the poster image or a placeholder if path is invalid
     */
    fun buildPosterUrl(movie: Movie, size: String = IMAGE_SIZE_W500): String {
        return movie.posterPath?.takeIf { it.isNotBlank() }?.let { path ->
            // Ensure the path starts with a forward slash
            val cleanPath = if (path.startsWith('/')) path else "/$path"
            "${BASE_IMAGE_URL}${size}${cleanPath}"
        } ?: DEFAULT_POSTER_URL
    }

    /**
     * Builds a URL for a movie backdrop image
     * @param movie The movie containing the backdrop path
     * @param size The desired image size (defaults to original)
     * @return Complete URL for the backdrop image or a placeholder if path is invalid
     */
    fun buildBackdropUrl(movie: Movie, size: String = IMAGE_SIZE_ORIGINAL): String {
        return movie.backdropPath?.takeIf { it.isNotBlank() }?.let { path ->
            // Ensure the path starts with a forward slash
            val cleanPath = if (path.startsWith('/')) path else "/$path"
            "${BASE_IMAGE_URL}${size}${cleanPath}"
        } ?: DEFAULT_BACKDROP_URL
    }
    
    /**
     * Builds a URL for a profile image
     * @param path The profile path from the API
     * @param size The desired image size (defaults to w185)
     * @return Complete URL for the profile image
     */
    fun buildProfileUrl(path: String?, size: String = IMAGE_SIZE_W185): String {
        return path?.takeIf { it.isNotBlank() }?.let { 
            val cleanPath = if (it.startsWith('/')) it else "/$it"
            "${BASE_IMAGE_URL}${size}${cleanPath}"
        } ?: DEFAULT_POSTER_URL
    }
}
