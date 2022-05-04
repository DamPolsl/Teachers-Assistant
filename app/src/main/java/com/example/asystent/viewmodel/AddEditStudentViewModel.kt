package com.example.asystent.viewmodel

import androidx.lifecycle.ViewModel
import com.example.asystent.entity.Subject

class AddEditStudentViewModel: ViewModel() {
    val subjectCheckedList: MutableList<Subject> = mutableListOf()
    var isEditing: Boolean = false
}