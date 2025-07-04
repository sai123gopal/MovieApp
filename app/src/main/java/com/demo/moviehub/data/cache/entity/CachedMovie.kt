package com.demo.moviehub.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.moviehub.data.model.Movie

@Entity(tableName = "cached_movies")
data class CachedMovie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double?,
    val cacheType: String, // "trending" or "popular"
    val cacheKey: String,  // timeWindow for trending, dateRange for popular
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMovie(): Movie = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity
    )
    
    companion object {
        fun fromMovie(movie: Movie, cacheType: String, cacheKey: String): CachedMovie = CachedMovie(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            posterPath = movie.posterPath,
            backdropPath = movie.backdropPath,
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage,
            voteCount = movie.voteCount,
            popularity = movie.popularity,
            cacheType = cacheType,
            cacheKey = cacheKey
        )
    }
}
