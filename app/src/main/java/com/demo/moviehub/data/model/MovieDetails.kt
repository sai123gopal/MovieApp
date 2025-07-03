package com.demo.moviehub.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val runtime: Int?,
    val genres: List<Genre>,
    val credits: Credits?
)

data class Genre(
    val id: Int,
    val name: String
)

data class Credits(
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    @SerializedName("profile_path")
    val profilePath: String?
)

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String,
    @SerializedName("profile_path")
    val profilePath: String?
)
