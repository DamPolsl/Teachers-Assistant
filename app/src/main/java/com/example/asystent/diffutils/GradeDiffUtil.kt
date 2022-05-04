package com.example.asystent.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.asystent.entity.Grade

class GradeDiffUtil(
    private var oldList: List<Grade>,
    private var newList: List<Grade>
): DiffUtil.Callback()  {
    override fun getOldListSize(): Int {
        return oldList.count()
    }

    override fun getNewListSize(): Int {
        return newList.count()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].gradeId == newList[newItemPosition].gradeId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return when {
            old.gradeId != new.gradeId -> false
            old.grade != new.grade -> false
            old.description != new.description -> false
            old.studentId != new.studentId -> false
            old.subjectId != new.subjectId -> false
            else -> true
        }
    }
}