package com.sun.kh_mvvm_architect_kotlin.screen.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sun.kh_mvvm_architect_kotlin.data.Task
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksDataSource
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksRepository

/**
 * Created by KenHoang16CDTH12 on 14/04/2019.
 * hoang.duongminh0221@gmail.com
 */
class StatisticsViewModel(
        private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error
    /**
     * Controls whether the stats are shown or a "No data" message.
     */
    private val _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean>
        get() = _empty

    private val _numberOfActiveTasks = MutableLiveData<Int>()
    val numberOfActiveTasks: LiveData<Int>
        get() = _numberOfActiveTasks

    private val _numberOfCompletedTasks = MutableLiveData<Int>()
    val numberOfCompletedTasks: LiveData<Int>
        get() = _numberOfCompletedTasks

    private var activeTasks = 0

    private var completedTasks = 0

    fun start() {
        loadStatistics()
    }

    private fun loadStatistics() {
        _dataLoading.value = true

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                _error.value = false
                computeStats(tasks)
            }

            override fun onDataNotAvailable() {
                _error.value = true
                activeTasks = 0
                completedTasks = 0
                updateDataBindingObservables()
            }
        })
    }

    /**
     * Called when new data is ready.
     */
    private fun computeStats(tasks: List<Task>) {
        var completed = 0
        var active = 0

        for (task in tasks) {
            if (task.isCompleted) {
                completed += 1
            } else {
                active += 1
            }
        }
        activeTasks = active
        completedTasks = completed

        updateDataBindingObservables()
    }

    private fun updateDataBindingObservables() {
        _numberOfCompletedTasks.value = completedTasks

        _numberOfActiveTasks.value = activeTasks

        _empty.value = activeTasks + completedTasks == 0
        _dataLoading.value = false
    }


}