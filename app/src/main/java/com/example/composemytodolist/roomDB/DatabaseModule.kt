package com.example.composemytodolist.roomDB

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.composemytodolist.roomDB.AppDatabase
import com.example.composemytodolist.roomDB.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "todo-database"
        )
            //.fallbackToDestructiveMigration() // 기존 데이터베이스를 삭제하고 새로 생성
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @Provides
    fun provideTodoDao(database: AppDatabase): TodoDao {
        return database.getTodoDao()
    }
}