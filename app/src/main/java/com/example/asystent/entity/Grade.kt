package com.example.asystent.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grade_table")
data class Grade(
    val description: String,
    val grade: String,
    val studentId: Int,
    val subjectId: Int,
    @PrimaryKey(autoGenerate = true) var gradeId: Int = 0
)