package com.sun.kh_mvvm_architect_kotlin.screen.addedittask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.sun.kh_mvvm_architect_kotlin.R
import com.sun.kh_mvvm_architect_kotlin.util.ADD_EDIT_RESULT_OK
import com.sun.kh_mvvm_architect_kotlin.util.obtainViewModel
import com.sun.kh_mvvm_architect_kotlin.util.replaceFragmentInActivity
import com.sun.kh_mvvm_architect_kotlin.util.setUpActionBar

class AddEditTaskActivity : AppCompatActivity(), AddEditTaskNavigator {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onTaskSaved() {
        setResult(ADD_EDIT_RESULT_OK)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_tasks_act)
        setUpActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        replaceFragmentInActivity(obtainViewFragment(), R.id.contentFrame)
        subscribeToNavigationChanges()
    }

    private fun subscribeToNavigationChanges() {
        // The activity observes the navigation events in the ViewModel
        obtainViewModel().taskUpdatedEvent.observe(this, Observer {
            this@AddEditTaskActivity.onTaskSaved()
        })
    }

    private fun obtainViewFragment() = supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            AddEditTaskFragment.newInstance().apply {
                arguments = Bundle().apply {
                    putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID,
                            intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID))
                }
            }

    fun obtainViewModel(): AddEditTaskViewModel = obtainViewModel(AddEditTaskViewModel::class.java)

    companion object {
        const val REQUEST_CODE = 1
    }
}
