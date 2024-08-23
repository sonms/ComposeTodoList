package com.example.composemytodolist.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemytodolist.repository.TodoRepository
import com.example.composemytodolist.roomDB.TodoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainTodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    // Ensure this is LiveData<List<TodoEntity>>
    val todoList: LiveData<List<TodoEntity>> = repository.getAllTodos()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTodo(title: String, content: String) {
        viewModelScope.launch {
            val newTodo = TodoEntity(
                title = title,
                content = content
            )
            repository.insertTodo(newTodo)
        }
    }

    fun deleteTodoById(id: Long) {
        viewModelScope.launch {
            repository.deleteTodoById(id)
        }
    }
}