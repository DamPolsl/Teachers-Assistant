package com.example.asystent

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.asystent.application.AsystentApplication
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.viewmodel.*
import com.google.android.material.button.MaterialButton

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view.findViewById<MaterialButton>(R.id.btn_clear_database)).setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            builder.setMessage("Czy na pewno chcesz wyczyścić bazę danych?")
                .setCancelable(false)
                .setPositiveButton("Tak") { _, _ ->
                    asystentViewModel.deleteAllStudents()
                    asystentViewModel.deleteAllSubjects()
                    asystentViewModel.deleteAllCrossRefs()
                    asystentViewModel.deleteAllGrades()
                    asystentViewModel.clearSqliteSequence()
                }
                .setNegativeButton("Nie") {dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    private val asystentViewModel: AsystentViewModel by activityViewModels{
        AsystentViewModelFactory((requireActivity().application as AsystentApplication).asystentRepository)
    }
}