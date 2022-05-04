package com.example.asystent.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_table")
data class Subject(
    val name: String,
    val timeFrom: String,
    val timeTo: String,
    val weekDay: String,
    val weekDayNumber: Int,
    @PrimaryKey(autoGenerate = true) var subjectId:Int = 0
)