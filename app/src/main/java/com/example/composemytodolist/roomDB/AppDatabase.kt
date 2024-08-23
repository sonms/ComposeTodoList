package com.example.composemytodolist.roomDB

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@RequiresApi(Build.VERSION_CODES.O)
@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    //구체적인 구현을 자식 클래스에서 수행하도록 하기 위함
    //반드시 서브클래스가 있어야 합니다
    //예를 들어, 모든 도형(Shape) 클래스는 draw() 메서드를 구현해야 한다고 강제할 수 있습니다.
    abstract fun getTodoDao(): TodoDao

    companion object {
        //글톤을 구현할 때 가장 중요한 조건인 '단 하나의 인스턴스만을 생성' 할 수 있도록 보장하는 것
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "todo-database"  //데베 이름
            ).build() //.fallbackToDestructiveMigration() // 옵션 추가: 데이터베이스 버전이 변경되었을 때 파괴적 마이그레이션 수행

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}