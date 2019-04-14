package com.sun.kh_mvvm_architect_kotlin.screen.tasks

import android.view.View
import com.sun.kh_mvvm_architect_kotlin.data.Task

interface TaskItemUserActionsListener {
    fun onCompleteChanged(task: Task, v: View)

    fun onTaskClicked(task: Task)
}
