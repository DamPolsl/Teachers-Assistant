package com.example.asystent.entity.relations

import androidx.room.Entity

@Entity(primaryKeys = ["studentId", "subjectId"])
data class StudentSubjectCrossRef(
    val studentId: Int,
    val subjectId: Int,
)