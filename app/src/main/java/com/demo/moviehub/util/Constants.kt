package com.demo.moviehub.util

object Constants {
    // TMDB API Configuration
    const val BASE_URL = "https://api.themoviedb.org/3/"
    
    // API Key is provided via BuildConfig
    val API_KEY: String = com.demo.moviehub.BuildConfig.TMDB_API_KEY
    
    // Network timeouts
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
        const val TRENDING_MOVIES = "trending/movie/day"
    }
}
