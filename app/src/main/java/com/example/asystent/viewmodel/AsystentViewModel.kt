package com.example.asystent.viewmodel

import androidx.lifecycle.*
import com.example.asystent.entity.Grade
import com.example.asystent.entity.Student
import com.example.asystent.entity.Subject
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.entity.relations.StudentWithSubjects
import com.example.asystent.entity.relations.SubjectWithStudents
import com.example.asystent.repository.AsystentRepository
import kotlinx.coroutines.*

class AsystentViewModel(private val repository: AsystentRepository) : ViewModel() {

    // subject
    val allSubjects: LiveData<List<Subject>> = repository.allSubjects.asLiveData()
    var isEditingSubjects: Boolean = false
    var subjectToEdit: Subject? = null

    fun insertSubject(subject: Subject) = viewModelScope.launch {
        repository.insertSubject(subject)
    }

    fun deleteSubject(subject: Subject) = viewModelScope.launch {
        repository.deleteSubject(subject)
    }

    fun deleteAllSubjects() = viewModelScope.launch {
        repository.deleteAllSubjects()
    }

    fun updateSubject(subject: Subject) = viewModelScope.launch {
        repository.updateSubject(subject)
    }

    // student
    val allStudents: LiveData<List<Student>> = repository.allStudents.asLiveData()
    var isEditingStudent = false
    var studentToEdit: Student? = null

    fun insertStudent(student: Student): Long = runBlocking {
        getIdAsync(student).await()
    }

    private suspend fun getIdAsync(student: Student): Deferred<Long> = withContext(Dispatchers.Default){
        async {
            repository.insertStudent(student)
        }
    }

    fun deleteStudent(student: Student) = viewModelScope.launch {
        repository.deleteStudent(student)
    }

    fun deleteAllStudents() = viewModelScope.launch {
        repository.deleteAllStudents()
    }

    fun updateStudent(student: Student) = viewModelScope.launch {
        repository.updateStudent(student)
    }

    // student - subject
    fun getStudentsOfSubject(subject: Subject): List<SubjectWithStudents> = runBlocking{
        getStOfSuAsync(subject).await()
    }

    private suspend fun getStOfSuAsync(subject: Subject): Deferred<List<SubjectWithStudents>> = withContext(
        Dispatchers.Default){
        async {
            repository.getStudentsOfSubject(subject.subjectId)
        }
    }

    fun getSubjectsOfStudent(student: Student): List<StudentWithSubjects> = runBlocking{
        getSuOfStAsync(student).await()
    }

    private suspend fun getSuOfStAsync(student: Student): Deferred<List<StudentWithSubjects>> = withContext(Dispatchers.Default){
        async {
            repository.getSubjectsOfStudent(student.studentId)
        }
    }

    // grade
    var isEditingGrade = false
    var gradeToEdit: Grade? = null

    fun insertGrade(grade: Grade) = viewModelScope.launch {
        repository.insertGrade(grade)
    }

    fun updateGrade(grade: Grade) = viewModelScope.launch {
        repository.updateGrade(grade)
    }

    fun deleteGrade(grade: Grade) = viewModelScope.launch {
        repository.deleteGrade(grade)
    }

    fun deleteAllGrades() = viewModelScope.launch {
        repository.deleteAllGrades()
    }

    fun deleteGradesOfStudent(studentId: Int) = viewModelScope.launch {
        repository.deleteGradesOfStudent(studentId)
    }

    fun deleteGradesOfSubject(subjectId: Int) = viewModelScope.launch {
        repository.deleteGradesOfSubject(subjectId)
    }

    fun getGradesOfStudentOfSubject(studentId: Int, subjectId: Int): LiveData<List<Grade>>{
        return repository.getGradesOfStudentOfSubject(studentId, subjectId).asLiveData()
    }

    // cross references
    fun insertStudentSubjectCrossRef(studentSubjectCrossRef: StudentSubjectCrossRef) = viewModelScope.launch{
        repository.insertStudentSubjectCrossRef(studentSubjectCrossRef)
    }

    fun deleteStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef) = viewModelScope.launch {
        repository.deleteStudentSubjectCrossRef(crossRef)
    }

    fun deleteAllCrossRefs() = viewModelScope.launch {
        repository.deleteAllCrossRefs()
    }

    // other
    fun clearSqliteSequence() = viewModelScope.launch {
        repository.clearSqliteSequence()
    }

}

class AsystentViewModelFactory(private val repository: AsystentRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AsystentViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AsystentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}