package com.example.composemytodolist.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composemytodolist.roomDB.TodoEntity
import com.example.composemytodolist.viewmodel.MainTodoViewModel
import androidx.compose.runtime.livedata.observeAsState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    todoViewModel: MainTodoViewModel = hiltViewModel()
) {
    val todoList by todoViewModel.todoList.observeAsState(emptyList())
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Todo List", style = MaterialTheme.typography.bodyMedium)
            LazyColumn {
                itemsIndexed(
                    items = todoList,
                    key = { index ,item ->
                        item.id

                    }
                ) { index, item ->
                    Log.d("HomeScreen", index.toString())
                    Log.d("HomeScreen", item.toString())
                    TodoItem(todo = item, onDelete = { todoViewModel.deleteTodoById(it.id) })
                }
            }

            Button(
                onClick = {
                    todoViewModel.addTodo(
                        title = "New Todo",
                        content = "This is a new todo"
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Todo")
            }
        }
    }
}


@Composable
fun TodoItem(todo: TodoEntity, onDelete: (TodoEntity) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(todo.title, style = MaterialTheme.typography.headlineMedium)
            Text(todo.content, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = { onDelete(todo) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}