package com.example.asystent.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class Student(
    val firstName: String,
    val lastName: String,
    val idNumber: String,
    @PrimaryKey(autoGenerate = true) var studentId: Int = 0
)