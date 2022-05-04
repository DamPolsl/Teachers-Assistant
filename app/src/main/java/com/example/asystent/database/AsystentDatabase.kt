package com.example.asystent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asystent.dao.AsystentDao
import com.example.asystent.entity.Grade
import com.example.asystent.entity.Student
import com.example.asystent.entity.Subject
import com.example.asystent.entity.relations.StudentSubjectCrossRef

@Database(
    entities = [
        Subject::class,
        Student::class,
        Grade::class,
        StudentSubjectCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AsystentDatabase: RoomDatabase() {

    abstract fun asystentDao(): AsystentDao

    companion object {
        @Volatile
        private var INSTANCE: AsystentDatabase? = null

        fun getDatabase(context: Context): AsystentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsystentDatabase::class.java,
                    "asystent_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}