package com.example.asystent.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.example.asystent.entity.Subject

class SubjectDiffUtil(
    private var oldList: List<Subject>,
    private var newList: List<Subject>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.count()
    }

    override fun getNewListSize(): Int {
        return newList.count()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].subjectId == newList[newItemPosition].subjectId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return when {
            old.subjectId != new.subjectId -> false
            old.name != new.name -> false
            old.timeFrom != new.timeFrom -> false
            old.timeTo != new.timeTo -> false
            old.weekDay != new.weekDay -> false
            else -> true
        }
    }
}