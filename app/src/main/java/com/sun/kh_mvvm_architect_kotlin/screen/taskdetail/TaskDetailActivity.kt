package com.sun.kh_mvvm_architect_kotlin.screen.taskdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sun.kh_mvvm_architect_kotlin.R
import com.sun.kh_mvvm_architect_kotlin.screen.addedittask.AddEditTaskActivity
import com.sun.kh_mvvm_architect_kotlin.screen.addedittask.AddEditTaskFragment
import com.sun.kh_mvvm_architect_kotlin.screen.taskdetail.TaskDetailFragment.Companion.REQUEST_EDIT_TASK
import com.sun.kh_mvvm_architect_kotlin.util.*

class TaskDetailActivity : AppCompatActivity(), TaskDetailNavigator {

    private lateinit var taskViewModel: TaskDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.task_detail_act)

        setUpActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        replaceFragmentInActivity(findOrCreateViewFragment(), R.id.contentFrame)

        taskViewModel = obtainViewModel()

        subscribeToNavigationChanges(taskViewModel)
    }

    private fun findOrCreateViewFragment() =
            supportFragmentManager.findFragmentById(R.id.contentFrame)
                    ?: TaskDetailFragment.newInstance(intent.getStringExtra(EXTRA_TASK_ID))

    private fun subscribeToNavigationChanges(viewModel: TaskDetailViewModel) {
        // The activity observes the navigation commands in the ViewModel
        val activity = this@TaskDetailActivity
        viewModel.run {
            editTaskCommand.observe(activity,
                    Observer { activity.onStartEditTask() })
            deleteTaskCommand.observe(activity,
                    Observer { activity.onTaskDeleted() })
        }
    }

    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == ADD_EDIT_RESULT_OK) {
                // If the result comes from the add/edit screen, it's an edit.
                setResult(EDIT_RESULT_OK)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onTaskDeleted() {
        setResult(DELETE_RESULT_OK)
        // If the task was deleted successfully, go back to the list.
        finish()
    }

    override fun onStartEditTask() {
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        val intent = Intent(this, AddEditTaskActivity::class.java).apply {
            putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
        }
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    fun obtainViewModel(): TaskDetailViewModel = obtainViewModel(TaskDetailViewModel::class.java)

    companion object {

        const val EXTRA_TASK_ID = "TASK_ID"

    }
}
