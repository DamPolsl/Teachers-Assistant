package com.example.asystent

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.adapter.SubjectClickDeleteInterface
import com.example.asystent.adapter.SubjectClickInterface
import com.example.asystent.adapter.SubjectListAdapter
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.Subject
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.viewmodel.*

class SubjectsFragment : Fragment(), SubjectClickDeleteInterface, SubjectClickInterface {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subjects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_subjects)
        val adapter = SubjectListAdapter(this,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))

        asystentViewModel.allSubjects.observe(requireActivity(), Observer { subjects ->
            subjects?.let {
                adapter.setData(it)
            }
        })

        (view.findViewById<Button>(R.id.btnAdd)).setOnClickListener {
            it.findNavController().navigate(R.id.action_subjectsFragment_to_addSubjectFragment)
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }

    override fun onDeleteIconClick(subject: Subject) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Czy na pewno chcesz usunąć przedmiot: ${subject.name}?")
            .setCancelable(false)
            .setPositiveButton("Tak") { _, _ ->
                // remove cross references
                asystentViewModel.getStudentsOfSubject(subject).forEach { subjectWithStudents ->
                    subjectWithStudents.students.forEach { student ->
                        asystentViewModel.deleteStudentSubjectCrossRef(StudentSubjectCrossRef(student.studentId, subject.subjectId))
                    }
                }
                // delete all grades of subject
                asystentViewModel.deleteGradesOfSubject(subject.subjectId)
                // delete subject
                asystentViewModel.deleteSubject(subject)
            }
            .setNegativeButton("Nie") {dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onSubjectClick(subject: Subject) {
        asystentViewModel.isEditingSubjects = true
        asystentViewModel.subjectToEdit = subject
        requireView().findNavController().navigate(R.id.action_subjectsFragment_to_addSubjectFragment)
    }
}