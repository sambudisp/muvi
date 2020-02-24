package com.sambudisp.muvi.database

import android.provider.BaseColumns

class DatabaseContract {

    internal class FavColumns : BaseColumns {
        companion object{
            const val TABLE_NAME = "table_fav"
            const val _ID = "_id"
            const val ID_FAV = "id_fav"
            const val DATE = "date"
            const val TITLE = "title"
            const val CATEGORY = "category"
            const val DESC = "desc"
            const val POSTER = "poster"
        }
    }
}