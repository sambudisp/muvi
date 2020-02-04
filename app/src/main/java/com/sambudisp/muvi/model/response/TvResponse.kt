package com.sambudisp.muvi.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvResponse(
    @field:SerializedName("results")
    val results: ArrayList<TVResponseResult>
) : Parcelable

@Parcelize
data class TVResponseResult(
    @field:SerializedName("id")
    val id : String? = null,

    @field:SerializedName("poster_path")
    val poster_path : String? = null,

    @field:SerializedName("original_name")
    val title : String? = null,

    @field:SerializedName("vote_average")
    val vote_average : String? = null,

    @field:SerializedName("overview")
    val overview : String? = null,

    @field:SerializedName("first_air_date")
    val release_date : String? = null
) : Parcelable
