package com.example.asystent.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.asystent.entity.Grade
import com.example.asystent.entity.Student

data class StudentWithGrades (
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentId",
        entityColumn = "studentId"
    )
    val grades: List<Grade>
)