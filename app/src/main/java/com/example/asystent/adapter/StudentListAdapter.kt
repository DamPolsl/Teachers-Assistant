package com.example.asystent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.R
import com.example.asystent.diffutils.StudentDiffUtil
import com.example.asystent.entity.Student

class StudentListAdapter(
    private val studentClickInterface: StudentClickInterface,
    private val studentClickDeleteInterface: StudentClickDeleteInterface
): RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

    private var oldStudentList = emptyList<Student>()

    inner class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.tv_name)
        val idNumberTV: TextView = itemView.findViewById(R.id.tv_id_number)
        val deleteIV: ImageView = itemView.findViewById(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListAdapter.StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.student_rv_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val current = oldStudentList[position]
        val name = "${current.firstName} ${current.lastName}"
        holder.nameTV.text = name
        holder.idNumberTV.text = current.idNumber

        holder.deleteIV.setOnClickListener {
            studentClickDeleteInterface.onDeleteIconClick(current)
        }

        holder.itemView.setOnClickListener {
            studentClickInterface.onStudentClick(current)
        }
    }

    fun setData(newStudentList: List<Student>){
        val diffUtil = StudentDiffUtil(oldStudentList, newStudentList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldStudentList = newStudentList
        diffResults.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return oldStudentList.count()
    }
}

interface StudentClickInterface{
    fun onStudentClick(student: Student)
}

interface StudentClickDeleteInterface{
    fun onDeleteIconClick(student: Student)
}