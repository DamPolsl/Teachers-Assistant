package com.example.asystent.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.asystent.entity.Student

class StudentDiffUtil(
    private var oldList: List<Student>,
    private var newList: List<Student>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.count()
    }

    override fun getNewListSize(): Int {
        return newList.count()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].studentId == newList[newItemPosition].studentId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return when {
            old.studentId != new.studentId -> false
            old.firstName != new.firstName -> false
            old.lastName != new.lastName -> false
            old.idNumber != new.idNumber -> false
            else -> true
        }
    }

}