package com.sun.kh_mvvm_architect_kotlin.data.source

import com.sun.kh_mvvm_architect_kotlin.data.Task


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
interface TasksDataSource {

    interface LoadTasksCallback {
        fun onTasksLoaded(tasks: List<Task>)

        fun onDataNotAvailable()
    }

    interface GetTaskCallback {
        fun onTaskLoaded(task: Task)

        fun onDataNotAvavilable()
    }

    fun getTasks(callback: LoadTasksCallback)

    fun getTask(taskId: String, callback: GetTaskCallback)

    fun saveTask(task: Task)

    fun completeTask(task: Task)

    fun completeTask(taskId: String)

    fun activateTask(task: Task)

    fun activateTask(taskId: String)

    fun clearCompletedTasks()

    fun refreshTasks()

    fun deleteAllTasks()

    fun deleteTask(taskId: String)
}