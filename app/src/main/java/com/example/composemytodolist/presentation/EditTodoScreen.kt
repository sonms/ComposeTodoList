package com.example.composemytodolist.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.composemytodolist.MainActivity
import com.example.composemytodolist.viewmodel.MainTodoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditTodoScreen(
    navController: NavController, // NavController를 매개변수로 추가
    todoViewModel: MainTodoViewModel = hiltViewModel()
) {
    var textTitle by remember { mutableStateOf("") }
    var textContent by remember { mutableStateOf("") }
    val textColor = MaterialTheme.colorScheme.onSecondary//동적으로 색상변경
    Column (
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
    ) {
        BasicTextField(
            value = textTitle,
            onValueChange = { textTitle = it },
            singleLine = false,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFBAE5F5),
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .padding(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "",
                        tint = Color.DarkGray,
                    )
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    innerTextField()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = textContent,
            onValueChange = {textContent = it},
            singleLine = true,
            textStyle = TextStyle (
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


        Button(
            onClick = {
                todoViewModel.addTodo(
                    title = textTitle,
                    content = textContent
                )
                navController.navigate(MainActivity.BottomNavItem.Home.screenRoute)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("+")
        }
    }
}