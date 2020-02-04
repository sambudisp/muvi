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

    @field:SerializedName("genres")
    val genres: ArrayList<MovieGenres>,

    @field:SerializedName("production_companies")
    val production_companies: ArrayList<MovieProductionCompanies>
) : Parcelable


@Parcelize
data class MovieGenres(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null
) : Parcelable

@Parcelize
data class MovieProductionCompanies(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null
) : Parcelable