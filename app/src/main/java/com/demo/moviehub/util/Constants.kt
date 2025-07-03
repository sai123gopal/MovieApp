package com.demo.moviehub.util

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    
    val API_KEY: String = com.demo.moviehub.BuildConfig.TMDB_API_KEY
    
    const val CONNECT_TIMEOUT = 30L // seconds
    const val READ_TIMEOUT = 30L // seconds

    const val ROUTE_HOME = "home"
    const val ROUTE_FAVORITES = "favorites"
    const val ROUTE_SETTINGS = "settings"
    const val ROUTE_MOVIE_DETAILS = "movie/{movieId}"
}
