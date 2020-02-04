package com.sambudisp.muvi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentModel(
    var poster : Int,
    var title : String,
    var description : String,
    var rate : String,
    var restriction : String,
    var price : String,
    var isPromo : Boolean,
    var category : String,
    var video : String,
    var itemCategory : String
) : Parcelable