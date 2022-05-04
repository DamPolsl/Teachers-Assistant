package com.example.asystent.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.asystent.entity.Grade
import javax.security.auth.Subject

data class SubjectWithGrades(
    @Embedded val subject:Subject,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "subjectId"
    )
    val grades: List<Grade>
)