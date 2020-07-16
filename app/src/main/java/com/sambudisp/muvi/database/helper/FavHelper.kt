package com.sambudisp.muvi.database.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.CATEGORY
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.ID_FAV
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion._ID
import com.sambudisp.muvi.database.DatabaseHelper

class FavHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: FavHelper? = null
        fun getInstance(context: Context): FavHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: FavHelper(context)
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun isOpen(): Boolean {
        try {
            if (database.isOpen) return true else return false
        } catch (e: Exception) {
            return false
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryByCategory(category: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$CATEGORY = ?",
            arrayOf(category),
            null,
            null,
            null,
            null
        )
    }

    fun queryByFavId(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$ID_FAV = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }


}