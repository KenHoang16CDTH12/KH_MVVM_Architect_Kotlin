package com.sun.kh_mvvm_architect_kotlin.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sun.kh_mvvm_architect_kotlin.data.Task


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
@Database(entities = [Task::class], version = 1)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao

    companion object {
        private var sInstance: ToDoDatabase? = null
        private val lock = Any()
        fun getInstance(context: Context): ToDoDatabase {
            synchronized(lock) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.applicationContext,
                        ToDoDatabase::class.java, "Database.db")
                        .build()
                }
                return sInstance!!
            }
        }
    }
}