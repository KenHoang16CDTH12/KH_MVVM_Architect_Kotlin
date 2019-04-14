package com.sun.kh_mvvm_architect_kotlin

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksRepository
import com.sun.kh_mvvm_architect_kotlin.screen.addedittask.AddEditTaskViewModel
import com.sun.kh_mvvm_architect_kotlin.screen.statistics.StatisticsViewModel
import com.sun.kh_mvvm_architect_kotlin.screen.taskdetail.TaskDetailViewModel
import com.sun.kh_mvvm_architect_kotlin.screen.tasks.TasksViewModel
import java.lang.IllegalArgumentException


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
class ViewModelFactory private constructor(
    private val tasksRepository: TasksRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(StatisticsViewModel::class.java) ->
                    StatisticsViewModel(tasksRepository)
                isAssignableFrom(TaskDetailViewModel::class.java) ->
                    TaskDetailViewModel(tasksRepository)
                isAssignableFrom(AddEditTaskViewModel::class.java) ->
                    AddEditTaskViewModel(tasksRepository)
                isAssignableFrom(TasksViewModel::class.java) ->
                    TasksViewModel(tasksRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var sInstance: ViewModelFactory? = null

        fun getInstance(application: Application) =
                sInstance ?: synchronized(ViewModelFactory::class.java) {
                    sInstance ?: ViewModelFactory(
                        Injection.provideTasksRepository(application.applicationContext)
                    ).also { sInstance = it }
                }

        @VisibleForTesting fun destroyInstance() { sInstance = null }
    }
}