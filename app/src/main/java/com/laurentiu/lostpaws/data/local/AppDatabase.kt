package com.laurentiu.lostpaws.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.laurentiu.lostpaws.data.local.dao.PetDao
import com.laurentiu.lostpaws.data.local.dao.UserDao
import com.laurentiu.lostpaws.data.local.entity.PetEntity
import com.laurentiu.lostpaws.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lost_paws.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
