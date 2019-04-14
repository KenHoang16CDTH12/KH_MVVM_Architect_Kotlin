package com.sun.kh_mvvm_architect_kotlin.screen.tasks

import androidx.annotation.DrawableRes
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
import com.sun.kh_mvvm_architect_kotlin.screen.addedittask.AddEditTaskActivity
import com.sun.kh_mvvm_architect_kotlin.util.ADD_EDIT_RESULT_OK
import com.sun.kh_mvvm_architect_kotlin.util.DELETE_RESULT_OK
import com.sun.kh_mvvm_architect_kotlin.util.EDIT_RESULT_OK

class TasksViewModel(
        private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Task>>().apply { value = emptyList() }
    val items: LiveData<List<Task>>
        get() = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _currentFilteringLabel = MutableLiveData<Int>()
    val currentFilteringLabel: LiveData<Int>
        get() = _currentFilteringLabel

    private val _noTasksLabel = MutableLiveData<Int>()
    val noTasksLabel: LiveData<Int>
        get() = _noTasksLabel

    private val _noTaskIconRes = MutableLiveData<Int>()
    val noTasksIconRes: LiveData<Int>
        get() = _noTaskIconRes

    private val _tasksAddViewVisible = MutableLiveData<Boolean>()
    val tasksAddViewVisible: LiveData<Boolean>
        get() = _tasksAddViewVisible

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    private var _currentFiltering = TasksFilterType.ALL_TASKS

    // Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>>
        get() = _openTaskEvent

    private val _newTaskEvent = MutableLiveData<Event<Unit>>()
    val newTaskEvent: LiveData<Event<Unit>>
        get() = _newTaskEvent

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    init {
        // Set initial state
        setFiltering(TasksFilterType.ALL_TASKS)
    }

    fun start() {
        loadTasks(false)
    }

    fun loadTasks(foreceUpdate: Boolean) {
        loadTasks(foreceUpdate, true)
    }

    fun setFiltering(requestType: TasksFilterType) {
        _currentFiltering = requestType

        // Depending on the filter type, set the filtering label, icon drawables, etc.`
        when (requestType) {
            TasksFilterType.ALL_TASKS -> {
                setFilter(R.string.label_all, R.string.no_tasks_all,
                        R.drawable.ic_assignment_turned_in_24dp, true)
            }
            TasksFilterType.ACTIVE_TASKS -> {
                setFilter(R.string.label_active, R.string.no_tasks_active,
                        R.drawable.ic_check_circle_24dp, false)
            }
            TasksFilterType.COMPLETED_TASKS -> {
                setFilter(R.string.label_completed, R.string.no_tasks_completed,
                        R.drawable.ic_verified_user_24dp, false)
            }
        }
    }

    private fun setFilter(@StringRes filteringLabelString: Int, @StringRes noTasksLabelString: Int,
                          @DrawableRes noTasksIconDrawable: Int, tasksAddVisible: Boolean) {
        _currentFilteringLabel.value = filteringLabelString
        _noTasksLabel.value = noTasksLabelString
        _noTaskIconRes.value = noTasksIconDrawable
        _tasksAddViewVisible.value = tasksAddVisible
    }

    fun clearCompletedTasks() {
        tasksRepository.clearCompletedTasks()
        _snackbarText.value = Event(R.string.completed_tasks_cleared)
        loadTasks(false, false)
    }

    fun completeTask(task: Task, completed: Boolean) {
        // Notify repository
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    fun addNewTask() {
        _newTaskEvent.value = Event(Unit)
    }

    /**
     * Called by the [TasksAdapter].
     */
    internal fun openTask(taskId: String) {
        _openTaskEvent.value = Event(taskId)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {
        if (AddEditTaskActivity.REQUEST_CODE == requestCode) {
            when (resultCode) {
                EDIT_RESULT_OK -> _snackbarText.setValue(
                        Event(R.string.successfully_saved_task_message)
                )
                ADD_EDIT_RESULT_OK -> _snackbarText.setValue(
                        Event(R.string.successfully_added_task_message)
                )
                DELETE_RESULT_OK -> _snackbarText.setValue(
                        Event(R.string.successfully_deleted_task_message)
                )
            }
        }
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [TasksDataSource]
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            _dataLoading.value = true
        }
        if (forceUpdate)
            tasksRepository.refreshTasks()

        tasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()
                // We filter the tasks based on the requestType
                for (task in tasks) {
                    when (_currentFiltering) {
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if (task.isActive) tasksToShow.add(task)
                        TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted) tasksToShow.add(task)
                    }
                }
                if (showLoadingUI) _dataLoading.value = false
                isDataLoadingError.value = false
                val itemsValue = ArrayList(tasksToShow)
                _items.value = itemsValue
            }

            override fun onDataNotAvailable() {
                isDataLoadingError.value = true
            }

        })
    }
}
