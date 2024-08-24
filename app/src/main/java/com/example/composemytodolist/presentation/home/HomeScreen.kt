package com.example.composemytodolist.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.composemytodolist.ui.theme.ComposeMyTodoListTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FakeHomeScreen(
    //todoViewModel: MainTodoViewModel = hiltViewModel()
    todoList: MutableList<TodoEntity> = mutableListOf<TodoEntity>()
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Todo List", style = MaterialTheme.typography.bodyMedium)
            LazyColumn {
                itemsIndexed(
                    items = todoList,
                    key = { _ ,item ->
                        item.id
                    }
                ) { index, item ->
                    Log.d("HomeScreen", index.toString())
                    Log.d("HomeScreen", item.toString())
                    TodoItem(todo = item, onDelete = { todoList.remove(item) }, onItemClick = { clickedTodo ->
                        // Handle the click event here
                        Log.d("HomeScreen", "Clicked item: ${clickedTodo.title}")
                        // For example, navigate to a new screen or show a dialog
                    })
                }
            }
        }

        FloatingActionButton(
            onClick = {
                todoList.add(
                    TodoEntity(
                        0L,
                        "new3",
                        "content3",
                        ""
                    )
                )
            },
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Delete")
        }

    }
}

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
                    key = { _ ,item ->
                        item.id

                    }
                ) { index, item ->
                    Log.d("HomeScreen", index.toString())
                    Log.d("HomeScreen", item.toString())
                    TodoItem(todo = item,
                        onDelete = { todoViewModel.deleteTodoById(it.id) },
                        onItemClick = { clickedTodo ->
                            // Handle the click event here
                            Log.d("HomeScreen", "Clicked item: ${clickedTodo.title}")
                            // For example, navigate to a new screen or show a dialog
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun TodoItem(todo: TodoEntity, onDelete: (TodoEntity) -> Unit, onItemClick: (TodoEntity) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(todo) },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(todo.title, style = MaterialTheme.typography.headlineSmall)
            Text(todo.content, style = MaterialTheme.typography.bodyLarge)
        }
        IconButton(onClick = { onDelete(todo) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
val todoList = mutableListOf<TodoEntity>(
    TodoEntity(
        0L,
        "new1",
        "content1",
        "test"
    ),
    TodoEntity(
        1L,
        "new2",
        "content2",
        "test"
    )
)

/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun defaultScreen() {
    FakeHomeScreen(todoList)
}*/
