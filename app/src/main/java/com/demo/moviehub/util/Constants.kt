package com.demo.moviehub.util

object Constants {
    // TMDB API Configuration
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "" // Replace with your actual API key
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    
    // Image sizes
    const val IMAGE_SIZE_500 = "w500"
    const val IMAGE_SIZE_ORIGINAL = "original"
    
    // Timeouts
    const val CONNECT_TIMEOUT = 30L // seconds
    const val READ_TIMEOUT = 30L // seconds
    
    // API Endpoints
    object Endpoints {
        const val MOVIE_POPULAR = "movie/popular"
        const val MOVIE_TOP_RATED = "movie/top_rated"
        const val MOVIE_UPCOMING = "movie/upcoming"
        const val MOVIE_NOW_PLAYING = "movie/now_playing"
        const val MOVIE_DETAILS = "movie/{movie_id}"
        const val MOVIE_CREDITS = "movie/{movie_id}/credits"
    }
}
