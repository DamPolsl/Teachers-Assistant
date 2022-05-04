package com.example.asystent

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asystent.adapter.StudentClickGradesInterface
import com.example.asystent.adapter.StudentsOfSubjectListAdapter
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.Student
import com.example.asystent.entity.Subject
import com.example.asystent.viewmodel.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddSubjectFragment : Fragment(), StudentClickGradesInterface {

    private lateinit var selectedTimeFromTextView: TextView
    private lateinit var selectedTimeToTextView: TextView
    private lateinit var addSubjectButton: MaterialButton
    private lateinit var subjectButtonBack: FloatingActionButton
    private lateinit var subjectNameEditText: EditText
    private lateinit var subjectDayOfWeekSpinner: Spinner
    private lateinit var studentsRecyclerView: RecyclerView

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val formattedTime: String = when {
            hourOfDay < 10 -> {
                if (minute < 10){
                    "0${hourOfDay}:0${minute}"
                } else {
                    "0${hourOfDay}:${minute}"
                }
            }
            else -> {
                if (minute < 10){
                    "${hourOfDay}:0${minute}"
                } else {
                    "${hourOfDay}:${minute}"
                }
            }
        }
        return formattedTime
    }

    private val timeFromListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            selectedTimeFromTextView.text = formatTime(hourOfDay, minute)
        }

    private val timeToListener: TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            selectedTimeToTextView.text = formatTime(hourOfDay, minute)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedTimeFromTextView = view.findViewById(R.id.tv_time_from_output)
        selectedTimeToTextView = view.findViewById(R.id.tv_time_to_output)
        subjectNameEditText = view.findViewById(R.id.et_subject_name)
        subjectDayOfWeekSpinner = view.findViewById(R.id.sp_day_of_week)
        studentsRecyclerView = view.findViewById(R.id.rv_students_of_subject)

        addSubjectButton = view.findViewById(R.id.btnSubjectAdd)
        subjectButtonBack = view.findViewById(R.id.btnBack)

        if(asystentViewModel.isEditingSubjects){
            asystentViewModel.isEditingSubjects = false
            addEditSubjectViewModel.isEditing = true
        }

        if(addEditSubjectViewModel.isEditing){
            "Edytuj przedmiot".also { addSubjectButton.text = it }

            val current: Subject? = asystentViewModel.subjectToEdit
            subjectNameEditText.setText(current?.name)
            subjectDayOfWeekSpinner.setSelection(current!!.weekDayNumber - 1)
            selectedTimeFromTextView.text = current.timeFrom
            selectedTimeToTextView.text = current.timeTo
        } else {
            "Dodaj przedmiot".also { addSubjectButton.text = it }
        }

        // recyclerview wyswietla studentow tylko jak edytujemy juz istniejacy
        if(addEditSubjectViewModel.isEditing){
            val adapter = StudentsOfSubjectListAdapter(this)
            studentsRecyclerView.adapter = adapter
            studentsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            studentsRecyclerView.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
            asystentViewModel.getStudentsOfSubject(asystentViewModel.subjectToEdit!!).forEach { subjectWithStudents ->
                adapter.setData(subjectWithStudents.students)
            }
        }

        addSubjectButton.setOnClickListener{
            if(updateSubject()){
                view.findNavController().navigate(R.id.action_addSubjectFragment_to_subjectsFragment)
            }
        }

        subjectButtonBack.setOnClickListener {
            it.findNavController().navigate(R.id.action_addSubjectFragment_to_subjectsFragment)
        }

        (view.findViewById<Button>(R.id.btn_set_time_from)).setOnClickListener {
            val timePicker = TimePickerDialog(activity, timeFromListener, 0, 0, true)
            timePicker.show()
        }

        (view.findViewById<Button>(R.id.btn_set_time_to)).setOnClickListener {
            val timePicker = TimePickerDialog(activity, timeToListener, 0, 0, true)
            timePicker.show()
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }

    private val addEditSubjectViewModel: AddEditSubjectViewModel by viewModels()

    override fun onGradesButtonClick(student: Student) {
        asystentViewModel.studentToEdit = student
        if(updateSubject()){
            requireView().findNavController().navigate(R.id.action_addSubjectFragment_to_gradesFragment)
        }
    }

    private fun updateSubject(): Boolean{
        val name:String = subjectNameEditText.text.toString()
        val weekDay = subjectDayOfWeekSpinner.selectedItem.toString()
        val timeFrom = selectedTimeFromTextView.text.toString()
        val timeTo = selectedTimeToTextView.text.toString()

        val stringArray = resources.getStringArray(R.array.week_days)
        val weekDayNumber:Int = stringArray.indexOf(weekDay) + 1

        if (name.isNotEmpty()){
            val newSubject = Subject(name,timeFrom,timeTo,weekDay,weekDayNumber)
            if (addEditSubjectViewModel.isEditing){
                newSubject.subjectId = asystentViewModel.subjectToEdit!!.subjectId
                asystentViewModel.updateSubject(newSubject)
            } else {
                asystentViewModel.insertSubject(newSubject)
            }
            asystentViewModel.subjectToEdit = newSubject
            return true
        } else {
            Toast.makeText(requireActivity(), "Pola nie mogą być puste!", Toast.LENGTH_SHORT).show()
        }
        return false
    }
}