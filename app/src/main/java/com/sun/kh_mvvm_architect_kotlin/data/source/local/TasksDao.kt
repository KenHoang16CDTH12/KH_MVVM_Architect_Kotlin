package com.sun.kh_mvvm_architect_kotlin.data.source.local

import androidx.room.*
import com.sun.kh_mvvm_architect_kotlin.data.Task


/**
 * Created by KenHoang16CDTH12 on 13/04/2019.
 * hoang.duongminh0221@gmail.com
 */
@Dao interface TasksDao {

    @Query("SELECT * FROM Tasks") fun getTasks(): List<Task>

    @Query("SELECT * FROM Tasks WHERE entryid = :taskId") fun getTaskById(taskId: String): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertTask(task: Task)

    @Update fun updateTask(task: Task): Int

    @Query("UPDATE tasks SET completed = :completed WHERE entryid = :taskId")
    fun updateCompleted(taskId: String, completed: Boolean)

    @Query("DELETE FROM Tasks WHERE entryid = :taskId") fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM Tasks") fun deleteTasks()

    @Query("DELETE FROM Tasks WHERE completed = 1") fun deleteCompletedTasks(): Int
}