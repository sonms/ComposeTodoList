package com.example.composemytodolist.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoDataTable ORDER BY createDate DESC")
    fun getTodoList(): LiveData<List<TodoEntity>> // 변경: List<TodoEntity> -> LiveData<List<TodoEntity>>

    @Query("SELECT * FROM TodoDataTable WHERE type = 'calendar' ORDER BY eventDate DESC")
    fun getCalendarList(): LiveData<List<TodoEntity>> // 캘린더 To-Do 리스트 조회

    @Query("SELECT * FROM TodoDataTable WHERE type = 'calendar' AND eventDate = :date ORDER BY eventDate DESC")
    fun getEventsByDate(date: String): LiveData<List<TodoEntity>> // 특정 날짜의 이벤트 조회

    @Insert
    suspend fun insertTodoList(todoData: TodoEntity) // Room과 ViewModel의 비동기 처리 일관성을 위해 suspend로 변경

    @Query("DELETE FROM TodoDataTable WHERE id = :id")
    suspend fun deleteTodoList(id: Long) // Room과 ViewModel의 비동기 처리 일관성을 위해 suspend로 변경
}