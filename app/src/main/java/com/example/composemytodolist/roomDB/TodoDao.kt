package com.example.composemytodolist.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoDataTable ORDER BY createDate DESC")
    fun getTodoList(): LiveData<List<TodoEntity>> // 변경: List<TodoEntity> -> LiveData<List<TodoEntity>>

    @Insert
    suspend fun insertTodoList(todoData: TodoEntity) // Room과 ViewModel의 비동기 처리 일관성을 위해 suspend로 변경

    @Query("DELETE FROM TodoDataTable WHERE id = :id")
    suspend fun deleteTodoList(id: Long) // Room과 ViewModel의 비동기 처리 일관성을 위해 suspend로 변경
}