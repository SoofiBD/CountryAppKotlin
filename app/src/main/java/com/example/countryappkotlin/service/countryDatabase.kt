package com.example.countryappkotlin.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.countryappkotlin.model.Model

@Database(entities = [Model::class], version = 1)
abstract class countryDatabase : RoomDatabase() {

        abstract fun countryDao(): CountryDao

        companion object {
                @Volatile
                private var instance: countryDatabase? = null

                operator fun invoke(context: Context) = instance ?: synchronized(Any()) {
                        instance ?: buildDatabase(context).also {
                                instance = it
                        }
                }

                private fun buildDatabase(context: Context) = Room.databaseBuilder(
                        context.applicationContext,
                        countryDatabase::class.java,
                        "countrydatabase"
                ).build()
        }
}