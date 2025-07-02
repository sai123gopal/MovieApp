package com.demo.moviehub.util

import com.demo.moviehub.data.model.Movie
import com.demo.moviehub.util.Constants.BASE_IMAGE_URL
import com.demo.moviehub.util.Constants.IMAGE_SIZE_500
import com.demo.moviehub.util.Constants.IMAGE_SIZE_ORIGINAL

object ImageUrlBuilder {
    fun buildPosterUrl(movie: Movie, size: String = IMAGE_SIZE_500): String {
        return movie.posterPath?.let { path ->
            "${BASE_IMAGE_URL}$size$path"
        } ?: ""
    }

    fun buildBackdropUrl(movie: Movie, size: String = IMAGE_SIZE_ORIGINAL): String {
        return movie.backdropPath?.let { path ->
            "${BASE_IMAGE_URL}$size$path"
        } ?: ""
    }
}
