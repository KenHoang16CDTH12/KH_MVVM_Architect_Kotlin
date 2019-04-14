package com.sun.kh_mvvm_architect_kotlin.data.source.remote

import android.os.Handler
import com.google.common.collect.Lists
import com.sun.kh_mvvm_architect_kotlin.data.Task
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksDataSource


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
object TasksRemoteDataSource : TasksDataSource {

    private const val  SERVICE_LATENCY_IN_MILLIS = 5000L

    private var mTasksServiceData = LinkedHashMap<String, Task>(2)

    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private fun addTask(title: String, description: String) {
        val newTask = Task(title, description)
        mTasksServiceData.put(newTask.id, newTask)
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        // Simulate network by delaying the execution.
        val tasks = Lists.newArrayList(mTasksServiceData.values)
        Handler().postDelayed({
            callback.onTasksLoaded(tasks)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val task = mTasksServiceData[taskId]
        with(Handler()) {
            if (task != null)
                postDelayed({ callback.onTaskLoaded(task) }, SERVICE_LATENCY_IN_MILLIS)
            else
                postDelayed({ callback.onDataNotAvavilable()}, SERVICE_LATENCY_IN_MILLIS)
        }
    }

    override fun saveTask(task: Task) {
        mTasksServiceData.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, task.id).apply {
            isCompleted = true
        }
        mTasksServiceData.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, task.id)
        mTasksServiceData.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    override fun clearCompletedTasks() {
        mTasksServiceData = mTasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }

    override fun refreshTasks() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllTasks() {
        mTasksServiceData.clear()
    }

    override fun deleteTask(taskId: String) {
        mTasksServiceData.remove(taskId)
    }
}