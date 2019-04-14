package com.sun.kh_mvvm_architect_kotlin

import android.content.Context
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksRepository
import com.sun.kh_mvvm_architect_kotlin.data.source.local.TasksLocalDataSource
import com.sun.kh_mvvm_architect_kotlin.data.source.local.ToDoDatabase
import com.sun.kh_mvvm_architect_kotlin.data.source.remote.TasksRemoteDataSource
import com.sun.kh_mvvm_architect_kotlin.util.AppExecutors


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
object Injection {

    fun provideTasksRepository(context: Context): TasksRepository {
        val database = ToDoDatabase.getInstance(context)
        return TasksRepository.getInstance(TasksRemoteDataSource,
            TasksLocalDataSource.getInstance(AppExecutors(), database.taskDao()))
    }
}