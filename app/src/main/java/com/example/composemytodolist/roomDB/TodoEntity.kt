package com.example.composemytodolist.roomDB

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Entity(tableName = "TodoDataTable")
data class TodoEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val content: String,
    @ColumnInfo
    val createDate: String = LocalDateTime.now().toString(),
    @ColumnInfo
    val type: String = "todo", // 추가: 일반 To-Do와 캘린더용 To-Do 구분을 위한 필드
    @ColumnInfo
    val eventDate: String? = null // 캘린더 이벤트의 날짜를 저장할 필드 (캘린더용 To-Do인 경우)
)
