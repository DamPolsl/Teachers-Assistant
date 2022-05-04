package com.example.asystent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.Grade
import com.example.asystent.viewmodel.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GradeAddEditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grade_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradeDescriptionET = view.findViewById<EditText>(R.id.et_grade_description)
        val gradeSpinner = view.findViewById<Spinner>(R.id.sp_grade)
        val studentNameTV = view.findViewById<TextView>(R.id.tv_student_name)
        val subjectNameTV = view.findViewById<TextView>(R.id.tv_subject_name)
        val addGradeButton = view.findViewById<MaterialButton>(R.id.btn_add_grade)
        val backButton = view.findViewById<FloatingActionButton>(R.id.btn_back)

        if(asystentViewModel.isEditingGrade){
            asystentViewModel.isEditingGrade = false
            addEditGradeViewModel.isEditing = true
        }

        if(addEditGradeViewModel.isEditing){
            val grade: Grade? = asystentViewModel.gradeToEdit
            gradeDescriptionET.setText(grade?.description)
            val gradesArray = requireContext().resources.getStringArray(R.array.grades)
            gradeSpinner.setSelection(gradesArray.indexOf(grade?.grade))
            addGradeButton.text = "Zmień ocenę"
        }

        val name = "${asystentViewModel.studentToEdit?.firstName} ${asystentViewModel.studentToEdit?.lastName}"
        studentNameTV.text = name
        subjectNameTV.text = "${asystentViewModel.subjectToEdit?.name}"

        addGradeButton.setOnClickListener {
            val newGrade = Grade(gradeDescriptionET.text.toString(),
                gradeSpinner.selectedItem.toString(),
                asystentViewModel.studentToEdit!!.studentId,
                asystentViewModel.subjectToEdit!!.subjectId
            )
            if(addEditGradeViewModel.isEditing){
                newGrade.gradeId = asystentViewModel.gradeToEdit!!.gradeId
                asystentViewModel.updateGrade(newGrade)
            } else {
                asystentViewModel.insertGrade(newGrade)
            }
            requireView().findNavController().navigate(R.id.action_gradeAddEditFragment_to_gradesFragment)
        }

        backButton.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_gradeAddEditFragment_to_gradesFragment)
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }

    private val addEditGradeViewModel: AddEditGradeViewModel by viewModels()
}