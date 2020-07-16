package com.sambudisp.muvi.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sambudisp.muvi.database.DatabaseContract.FavColumns
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbdicoding"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
                " (${FavColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${FavColumns.ID_FAV} TEXT NOT NULL," +
                " ${FavColumns.DATE} TEXT NOT NULL," +
                " ${FavColumns.TITLE} TEXT NOT NULL," +
                " ${FavColumns.CATEGORY} TEXT NOT NULL," +
                " ${FavColumns.DESC} TEXT NOT NULL," +
                " ${FavColumns.POSTER} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}