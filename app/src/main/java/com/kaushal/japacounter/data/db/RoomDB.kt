package com.kaushal.japacounter.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kaushal.japacounter.data.JapaInfoRoomEntity


@Database(entities = [JapaInfoRoomEntity::class], version = 1, exportSchema = false)
@TypeConverters(com.kaushal.japacounter.data.db.TypeConverters::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDataBase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "Japa"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}