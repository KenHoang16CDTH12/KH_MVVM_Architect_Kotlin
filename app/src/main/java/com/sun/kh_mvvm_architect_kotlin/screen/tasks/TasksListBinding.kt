package com.sun.kh_mvvm_architect_kotlin.screen.tasks

import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.sun.kh_mvvm_architect_kotlin.data.Task


/**
 * Created by KenHoang16CDTH12 on 14/04/2019.
 * hoang.duongminh0221@gmail.com
 */
object TasksListBinding {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items: List<Task>) {
        with(listView.adapter as TasksAdapter) {
            replaceData(items)
        }
    }
}