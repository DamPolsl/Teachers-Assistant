package com.example.asystent

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.adapter.*
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.Student
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.viewmodel.*
import com.google.android.material.button.MaterialButton

class StudentsFragment : Fragment(), StudentClickDeleteInterface, StudentClickInterface {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_students)
        val adapter = StudentListAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))

        asystentViewModel.allStudents.observe(requireActivity()) { studentsList ->
            studentsList?.let {
                adapter.setData(it)
            }
        }

        (view.findViewById<MaterialButton>(R.id.btnAddStudent)).setOnClickListener {
            it.findNavController().navigate(R.id.action_studentsFragment_to_addStudentFragment)
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }

    override fun onStudentClick(student: Student) {
        asystentViewModel.isEditingStudent = true
        asystentViewModel.studentToEdit = student
        requireView().findNavController().navigate(R.id.action_studentsFragment_to_addStudentFragment)
    }

    override fun onDeleteIconClick(student: Student) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Czy na pewno chcesz usunąć studenta: ${student.firstName} ${student.lastName}?")
            .setCancelable(false)
            .setPositiveButton("Tak") { _, _ ->
                // remove cross references
                asystentViewModel.getSubjectsOfStudent(student).forEach { studentWithSubjects ->
                    studentWithSubjects.subjects.forEach { subject ->
                        asystentViewModel.deleteStudentSubjectCrossRef(StudentSubjectCrossRef(student.studentId, subject.subjectId))
                    }
                }
                // delete students grades
                asystentViewModel.deleteGradesOfStudent(student.studentId)
                // delete the student
                asystentViewModel.deleteStudent(student)
            }
            .setNegativeButton("Nie") {dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}