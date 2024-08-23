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
    val createDate: String = LocalDateTime.now().toString()
)
