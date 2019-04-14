package com.sun.kh_mvvm_architect_kotlin.screen.taskdetail

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sun.kh_mvvm_architect_kotlin.Event
import com.sun.kh_mvvm_architect_kotlin.R
import com.sun.kh_mvvm_architect_kotlin.data.Task
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksDataSource
import com.sun.kh_mvvm_architect_kotlin.data.source.TasksRepository


/**
 * Created by KenHoang16CDTH12 on 14/04/2019.
 * hoang.duongminh0221@gmail.com
 */
class TaskDetailViewModel(private val tasksRepository: TasksRepository)
    : ViewModel(), TasksDataSource.GetTaskCallback {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean>
        get() = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _editTaskCommand = MutableLiveData<Event<Unit>>()
    val editTaskCommand: LiveData<Event<Unit>>
        get() = _editTaskCommand

    private val _deleteTaskCommand = MutableLiveData<Event<Unit>>()
    val deleteTaskCommand: LiveData<Event<Unit>>
        get() = _deleteTaskCommand

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    // This LiveData depends on another so we can use a transformation.
    val completed: LiveData<Boolean> = Transformations.map(_task) {
        input -> input.isCompleted
    }

    val taskId: String?
        get() = _task.value?.id

    fun deleteTask() {
        taskId?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskCommand.value = Event(Unit)
        }
    }

    fun editTask() {
        _editTaskCommand.value = Event(Unit)
    }

    fun setCompleted(completed: Boolean) {
        val task = _task.value ?: return
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun start(taskId: String?) {
        if (taskId != null) {
            _dataLoading.value = true
            tasksRepository.getTask(taskId, this)
        }
    }

    private fun setTask(task: Task?) {
        this._task.value = task
        _isDataAvailable.value = task != null
    }

    override fun onTaskLoaded(task: Task) {
        setTask(task)
        _dataLoading.value = false
    }

    override fun onDataNotAvavilable() {
        _task.value = null
        _dataLoading.value = false
        _isDataAvailable.value = false
    }

    fun onRefresh() {
        taskId?.let { start(it) }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }
}