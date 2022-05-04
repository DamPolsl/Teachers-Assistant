package com.example.asystent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.adapter.PickSubjectListAdapter
import com.example.asystent.adapter.CheckboxClickInterface
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.Student
import com.example.asystent.entity.Subject
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.viewmodel.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddEditStudentFragment : Fragment(), CheckboxClickInterface{

    private lateinit var firstNameET: EditText
    private lateinit var lastNameET: EditText
    private lateinit var idNumberET: EditText
    private lateinit var addStudentButton: MaterialButton
    private lateinit var backButton: FloatingActionButton
    private lateinit var subjectsRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstNameET = view.findViewById(R.id.et_first_name)
        lastNameET = view.findViewById(R.id.et_last_name)
        idNumberET = view.findViewById(R.id.et_id)
        addStudentButton = view.findViewById(R.id.btnStudentAdd)
        backButton = view.findViewById(R.id.btnBack)
        subjectsRV = view.findViewById(R.id.rv_pick_subjects)


        if (asystentViewModel.isEditingStudent){
            asystentViewModel.isEditingStudent = false
            addEditStudentViewModel.isEditing = true
        }
        if(addEditStudentViewModel.isEditing){
            "Edytuj studenta".also { addStudentButton.text = it }

            val current: Student? = asystentViewModel.studentToEdit
            firstNameET.setText(current?.firstName)
            lastNameET.setText(current?.lastName)
            idNumberET.setText(current?.idNumber)
        } else {
            "Dodaj studenta".also { addStudentButton.text = it }
        }

        //recyclerview stuff
        val adapter = PickSubjectListAdapter(this)
        subjectsRV.adapter = adapter
        subjectsRV.layoutManager = LinearLayoutManager(requireActivity())
        subjectsRV.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        asystentViewModel.allSubjects.observe(requireActivity(), { subjects ->
            subjects?.let { adapter.setData(it) }
        })
        if(addEditStudentViewModel.isEditing){
            asystentViewModel.getSubjectsOfStudent(asystentViewModel.studentToEdit!!).forEach { studentWithSubjects ->
                studentWithSubjects.subjects.forEach { subject ->
                    addEditStudentViewModel.subjectCheckedList.add(subject)
                    adapter.checkedSubjects.add(subject)
                }
            }
        }


        addStudentButton.setOnClickListener {
            val firstName: String = firstNameET.text.toString()
            val lastName: String = lastNameET.text.toString()
            val idNumber: String = idNumberET.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && idNumber.isNotEmpty()){
                val student = Student(firstName, lastName, idNumber)
                if (addEditStudentViewModel.isEditing){
                    student.studentId = asystentViewModel.studentToEdit!!.studentId
                    asystentViewModel.updateStudent(student)
                    asystentViewModel.getSubjectsOfStudent(student).forEach { studentWithSubjects ->
                        studentWithSubjects.subjects.forEach { subject ->
                            asystentViewModel.deleteStudentSubjectCrossRef(StudentSubjectCrossRef(student.studentId, subject.subjectId))
                        }
                    }
                    for(subject in adapter.checkedSubjects){
                        asystentViewModel.insertStudentSubjectCrossRef(StudentSubjectCrossRef(asystentViewModel.studentToEdit!!.studentId, subject.subjectId))
                    }
                } else {
                    student.studentId = asystentViewModel.insertStudent(student).toInt()
                    for(subject in adapter.checkedSubjects){
                        asystentViewModel.insertStudentSubjectCrossRef(StudentSubjectCrossRef(student.studentId, subject.subjectId))
                    }
                }
                it.findNavController().navigate(R.id.action_addStudentFragment_to_studentsFragment)
            } else {
                Toast.makeText(requireActivity(), "Pola nie mogą być puste!", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_addStudentFragment_to_studentsFragment)
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }

    private val addEditStudentViewModel: AddEditStudentViewModel by viewModels()

    override fun onCheckboxChecked(subject: Subject, adapter: PickSubjectListAdapter) {
        adapter.checkedSubjects.add(subject)
        addEditStudentViewModel.subjectCheckedList.add(subject)
    }

    override fun onCheckboxUnchecked(subject: Subject, adapter: PickSubjectListAdapter) {
        adapter.checkedSubjects.remove(subject)
        addEditStudentViewModel.subjectCheckedList.remove(subject)
    }
}