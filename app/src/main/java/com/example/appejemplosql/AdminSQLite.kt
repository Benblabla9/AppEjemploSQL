package com.example.appejemplosql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase.CursorFactory

class AdminSQLite (context: Context, name:String, factory: CursorFactory?, version: Int): SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table articulos (codigo int primary key, descripcion text, precio real)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldversion:Int, newVersion: Int) {

    }

}