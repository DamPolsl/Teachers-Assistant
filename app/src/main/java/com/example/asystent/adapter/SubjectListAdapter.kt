package com.example.asystent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.R
import com.example.asystent.diffutils.SubjectDiffUtil
import com.example.asystent.entity.Subject

class SubjectListAdapter(
    private val subjectClickInterface: SubjectClickInterface,
    private val subjectClickDeleteInterface: SubjectClickDeleteInterface
    ) : RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder>(){

    private var oldSubjectList = emptyList<Subject>()

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectNameTV: TextView = itemView.findViewById(R.id.tv_name)
        val dayOfWeekTV: TextView = itemView.findViewById(R.id.tv_day_of_week)
        val timeFromTV: TextView = itemView.findViewById(R.id.tv_time_from)
        val timeToTV: TextView = itemView.findViewById(R.id.tv_time_to)
        val deleteIV: ImageView = itemView.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.subject_rv_item, parent, false)
        return SubjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val current = oldSubjectList[position]
        holder.subjectNameTV.text = current.name
        holder.dayOfWeekTV.text = current.weekDay
        holder.timeFromTV.text = current.timeFrom
        holder.timeToTV.text = current.timeTo

        holder.deleteIV.setOnClickListener {
            subjectClickDeleteInterface.onDeleteIconClick(current)
        }

        holder.itemView.setOnClickListener {
            subjectClickInterface.onSubjectClick(current)
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

interface SubjectClickInterface{
    fun onSubjectClick(subject: Subject)
}

interface SubjectClickDeleteInterface{
    fun onDeleteIconClick(subject: Subject)
}