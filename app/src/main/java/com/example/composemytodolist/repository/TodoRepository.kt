package com.example.composemytodolist.repository

import androidx.lifecycle.LiveData
import com.example.composemytodolist.roomDB.TodoDao
import com.example.composemytodolist.roomDB.TodoEntity
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {
    // 모든 할 일 가져오기
    fun getAllTodos(): LiveData<List<TodoEntity>> = todoDao.getTodoList()

    // 특정 날짜의 이벤트 가져오기
    fun getEventsByDate(date: String): LiveData<List<TodoEntity>> = todoDao.getEventsByDate(date)


    // 할 일 삽입
    suspend fun insertTodo(todo: TodoEntity) {
        todoDao.insertTodoList(todo)
    }

    // 할 일 삭제
    suspend fun deleteTodoById(id: Long) {
        todoDao.deleteTodoList(id)
    }
}