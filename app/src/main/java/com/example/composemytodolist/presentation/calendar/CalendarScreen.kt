package com.example.composemytodolist.presentation.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CalendarScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "캘린더", modifier = Modifier.align(Alignment.Center))
    }
}