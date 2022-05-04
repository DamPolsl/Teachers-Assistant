package com.example.asystent

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.adapter.GradeClickInterface
import com.example.asystent.adapter.GradeListAdapter
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.Grade
import com.example.asystent.viewmodel.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.round

class GradesFragment : Fragment(), GradeClickInterface {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grades, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val student = asystentViewModel.studentToEdit
        val subject = asystentViewModel.subjectToEdit
        var average: Float = 0f

        val studentNameTV = view.findViewById<TextView>(R.id.tv_student_name)
        val subjectNameTV = view.findViewById<TextView>(R.id.tv_subject_name)
        val averageTV = view.findViewById<TextView>(R.id.tv_average)
        val gradesRV = view.findViewById<RecyclerView>(R.id.rv_grades)
        val addGradeButton = view.findViewById<MaterialButton>(R.id.btn_add_grade)
        val backButton = view.findViewById<FloatingActionButton>(R.id.btn_back)

        val name = "${student?.firstName} ${student?.lastName}"

        studentNameTV.text = name
        subjectNameTV.text = "${subject?.name}"

        val adapter = GradeListAdapter(this)
        gradesRV.adapter = adapter
        gradesRV.layoutManager = LinearLayoutManager(requireActivity())
        gradesRV.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        asystentViewModel.getGradesOfStudentOfSubject(student!!.studentId, subject!!.subjectId).observe(requireActivity()) {
            adapter.setData(it)
            var sum = 0f
            it.forEach { grade ->
                sum += grade.grade.toFloat()
            }
            average = round(sum / it.count() * 100) / 100
            if (!average.isNaN()) {
                averageTV.text = average.toString()
            } else {
                averageTV.text = ""
            }
        }

        addGradeButton.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_gradesFragment_to_gradeAddEditFragment)
        }

        backButton.setOnClickListener {
            asystentViewModel.isEditingSubjects = true
            it.findNavController().navigate(R.id.action_gradesFragment_to_addSubjectFragment)
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }

    override fun onGradeEditClicked(grade: Grade) {
        asystentViewModel.isEditingGrade = true
        asystentViewModel.gradeToEdit = grade
        requireView().findNavController().navigate(R.id.action_gradesFragment_to_gradeAddEditFragment)
    }

    override fun onGradeDeleteClicked(grade: Grade) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Czy na pewno chcesz usunąć ocenę: ${grade.description}?")
            .setCancelable(false)
            .setPositiveButton("Tak") {_, _ ->
                asystentViewModel.deleteGrade(grade)
            }
            .setNegativeButton("Nie"){dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}