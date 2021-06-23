package com.klim.habrareader.data.db

import android.app.Application
import androidx.room.Room

object DbHelper {
    private const val DB_NAME = "habrareader.db"
    private lateinit var db: AppDatabase
    fun init(application: Application) {
        db = Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
            .addMigrations()
            .build()
    }

    fun get(): AppDatabase {
        return db
    }
}