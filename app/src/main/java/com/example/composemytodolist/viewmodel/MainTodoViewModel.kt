package com.example.composemytodolist.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.composemytodolist.repository.TodoRepository
import com.example.composemytodolist.roomDB.TodoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


@HiltViewModel
class MainTodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    val todoList: LiveData<List<TodoEntity>> = repository.getAllTodos()

    // 현재 선택된 날짜를 저장하는 LiveData
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    // _selectedDate가 변경될 때마다 자동으로 업데이트되는 calendarTodoList
    val calendarTodoList: LiveData<List<TodoEntity>> = _selectedDate.switchMap { date ->
        repository.getEventsByDate(date)
    }

    // 현재 선택된 월의 모든 이벤트를 저장하는 LiveData
    private val _selectedMonth = MutableLiveData<YearMonth>()
    @RequiresApi(Build.VERSION_CODES.O)
    val selectedMonthEvents: LiveData<List<TodoEntity>> = _selectedMonth.switchMap { month ->
        val formattedMonth = String.format("%02d", month.monthValue) // 월을 두 자리로 포맷
        repository.getEventsByMonth(month.year.toString(), formattedMonth)
    }

    // 선택된 월을 설정하는 함수 (월의 모든 이벤트 가져오기)
    fun fetchEventsByMonth(yearMonth: YearMonth) {
        _selectedMonth.value = yearMonth
    }

    // 선택된 날짜를 설정하는 함수
    fun fetchEventsByDate(date: String) {
        _selectedDate.value = date
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCalendarTodo(title: String, content: String, eventDate : String) {
        viewModelScope.launch {
            val newTodo = TodoEntity(
                title = title,
                content = content,
                type = "calendar",
                eventDate = eventDate
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