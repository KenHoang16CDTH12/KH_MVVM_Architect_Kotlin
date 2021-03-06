package com.sun.kh_mvvm_architect_kotlin.screen.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import com.sun.kh_mvvm_architect_kotlin.data.Task
import com.sun.kh_mvvm_architect_kotlin.databinding.TaskItemBinding

class TasksAdapter(
        private var tasks: List<Task>,
        private val tasksViewModel: TasksViewModel
) : BaseAdapter() {

    fun replaceData(tasks: List<Task>) {
        setList(tasks)
    }

    override fun getCount() = tasks.size

    override fun getItem(position: Int) = tasks[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val binding: TaskItemBinding
        binding = if (view == null) {
            // Inflate
            val inflate = LayoutInflater.from(viewGroup?.context)
            // Create the binding
            TaskItemBinding.inflate(inflate, viewGroup, false)
        } else {
            // Recycling view
            DataBindingUtil.getBinding(view) ?: throw IllegalStateException()
        }
        val userActionsListener = object : TaskItemUserActionsListener {
            override fun onCompleteChanged(task: Task, v: View) {
                val checked = (v as CheckBox).isChecked
                tasksViewModel.completeTask(task, checked)
            }

            override fun onTaskClicked(task: Task) {
                tasksViewModel.openTask(task.id)
            }
        }

        with(binding) {
            task = tasks[position]
            listener = userActionsListener
            executePendingBindings()
        }

        return binding.root
    }

    private fun setList(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}
