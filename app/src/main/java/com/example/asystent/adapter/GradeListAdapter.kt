package com.example.asystent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.R
import com.example.asystent.diffutils.GradeDiffUtil
import com.example.asystent.entity.Grade

class GradeListAdapter(
    private val gradeClickInterface: GradeClickInterface
): RecyclerView.Adapter<GradeListAdapter.GradeViewHolder>() {

    private var oldGradeList = emptyList<Grade>()

    class GradeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val gradeTV: TextView = itemView.findViewById(R.id.tv_grade)
        val gradeDescTV: TextView = itemView.findViewById(R.id.tv_grade_description)
        val editIV: ImageView = itemView.findViewById(R.id.iv_edit)
        val deleteIV: ImageView = itemView.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.grade_rv_item, parent, false)
        return GradeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val current = oldGradeList[position]
        holder.gradeTV.text = current.grade
        holder.gradeDescTV.text = current.description
        holder.editIV.setOnClickListener {
            gradeClickInterface.onGradeEditClicked(current)
        }
        holder.deleteIV.setOnClickListener {
            gradeClickInterface.onGradeDeleteClicked(current)
        }
    }

    fun setData(newGradeList: List<Grade>){
        val diffUtil = GradeDiffUtil(oldGradeList, newGradeList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldGradeList = newGradeList
        diffResults.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return oldGradeList.count()
    }
}

interface GradeClickInterface{
    fun onGradeEditClicked(grade: Grade)
    fun onGradeDeleteClicked(grade: Grade)
}