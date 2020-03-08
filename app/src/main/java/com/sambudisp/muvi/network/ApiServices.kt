package com.sambudisp.muvi.network

import com.sambudisp.muvi.model.response.MovieDetailResponse
import com.sambudisp.muvi.model.response.MovieResponse
import com.sambudisp.muvi.model.response.TvDetailResponse
import com.sambudisp.muvi.model.response.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    //movie list
    @Headers("Content-Type:application/json")
    @GET("discover/movie")
    fun movie(
        @Query("api_key") request: String?,
        @Query("language") language: String?
    ): Call<MovieResponse>

    //movie detail
    @Headers("Content-Type:application/json")
    @GET("movie/{movie_id}")
    fun movieDetail(
        @Path("movie_id") id: String?,
        @Query("api_key") request: String?,
        @Query("language") language: String?
    ): Call<MovieDetailResponse>

    //tv list
    @Headers("Content-Type:application/json")
    @GET("discover/tv")
    fun tv(
        @Query("api_key") request: String?,
        @Query("language") language: String?
    ): Call<TvResponse>

    //tv detail
    @Headers("Content-Type:application/json")
    @GET("tv/{tv_id}")
    fun tvDetail(
        @Path("tv_id") id: String?,
        @Query("api_key") request: String?,
        @Query("language") language: String?
    ): Call<TvDetailResponse>


    //movie search
    @Headers("Content-Type:application/json")
    @GET("search/movie")
    fun movieSearch(
        @Query("api_key") request: String?,
        @Query("language") language: String?,
        @Query("query") keyWord: String?
    ): Call<MovieResponse>

    //tv list
    @Headers("Content-Type:application/json")
    @GET("search/tv")
    fun tvSearch(
        @Query("api_key") request: String?,
        @Query("language") language: String?,
        @Query("query") keyWord: String?
    ): Call<TvResponse>


}

