package com.example.asystent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.R
import com.example.asystent.diffutils.SubjectDiffUtil
import com.example.asystent.entity.Subject

class PickSubjectListAdapter(
    private val checkboxClickInterface: CheckboxClickInterface
): RecyclerView.Adapter<PickSubjectListAdapter.SubjectViewHolder>(){

    private var oldSubjectList = emptyList<Subject>()
    val checkedSubjects: MutableList<Subject> = mutableListOf()

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectNameTV: TextView = itemView.findViewById(R.id.tv_name)
        val subjectCheckBox: CheckBox = itemView.findViewById(R.id.cb_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pick_subject_rv_item, parent, false)
        return SubjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val current = oldSubjectList[position]
        holder.subjectNameTV.text = current.name
        holder.subjectCheckBox.isChecked = (current in checkedSubjects)
        holder.subjectCheckBox.setOnClickListener {
            if (holder.subjectCheckBox.isChecked){
                checkboxClickInterface.onCheckboxChecked(current, this)
            } else {
                checkboxClickInterface.onCheckboxUnchecked(current, this)
            }
        }
    }

    fun setData(newSubjectList: List<Subject>){
        val diffUtil = SubjectDiffUtil(oldSubjectList, newSubjectList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldSubjectList = newSubjectList
        diffResults.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return oldSubjectList.count()
    }
}

interface CheckboxClickInterface{
    fun onCheckboxChecked(subject: Subject, adapter: PickSubjectListAdapter)
    fun onCheckboxUnchecked(subject: Subject, adapter: PickSubjectListAdapter)
}