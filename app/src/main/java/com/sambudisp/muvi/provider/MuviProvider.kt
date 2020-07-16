package com.sambudisp.muvi.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.sambudisp.muvi.database.DatabaseContract.AUTHORITY
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.sambudisp.muvi.database.helper.FavHelper

class MuviProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private const val FAV_CATEGORY = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favHelper: FavHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                FAV_ID
            )
//            sUriMatcher.addURI(
//                AUTHORITY,
//                "$TABLE_NAME/#",
//                FAV_CATEGORY
//            )
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            FAV -> cursor = favHelper.queryAll()
            FAV_ID -> cursor = favHelper.queryByFavId(uri.lastPathSegment.toString())
            //FAV_CATEGORY-> cursor = favHelper.queryByCategory(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        Log.d("URINYA", uri.lastPathSegment.toString())
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAV) {
            sUriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favHelper.update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> favHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

}
