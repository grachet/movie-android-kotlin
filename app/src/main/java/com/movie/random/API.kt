package com.movie.random

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("movie/popular")
    fun getPopularMovies(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlaying(
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page: Int
    ): Call<GetMoviesResponse>

    @GET("movie/latest")
    fun getLatest(
            @Query("api_key") apiKey: String = API_KEY
    ): Call<Movie>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendations(
            @Path("movie_id") movieId: Long,
            @Query("api_key") apiKey: String = API_KEY,
            @Query("page") page: Int
    ): Call<GetMoviesResponse>
}