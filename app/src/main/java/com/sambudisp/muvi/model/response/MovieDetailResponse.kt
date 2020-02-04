package com.sambudisp.muvi.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailResponse(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("poster_path")
    val poster_path: String? = null,

    @field:SerializedName("backdrop_path")
    val backdrop_path: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("vote_average")
    val vote_average: String? = null,

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("release_date")
    val release_date: String? = null,

    @field:SerializedName("homepage")
    val homepage: String? = null,

    //Perbedaan data
    @field:SerializedName("number_of_episodes")
    val number_of_episodes: String? = null,

    @field:SerializedName("number_of_seasons")
    val number_of_seasons: String? = null
) : Parcelable