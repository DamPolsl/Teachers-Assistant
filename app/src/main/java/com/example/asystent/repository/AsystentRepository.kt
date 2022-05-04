package com.example.asystent.repository

import androidx.annotation.WorkerThread
import com.example.asystent.dao.AsystentDao
import com.example.asystent.entity.Grade
import com.example.asystent.entity.Student
import com.example.asystent.entity.Subject
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.entity.relations.StudentWithSubjects
import com.example.asystent.entity.relations.SubjectWithStudents
import kotlinx.coroutines.flow.Flow

class AsystentRepository(private val asystentDao: AsystentDao) {

    // student

    val allStudents: Flow<List<Student>> = asystentDao.getAllStudents()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertStudent(student: Student): Long{
        return asystentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: Student){
        asystentDao.updateStudent(student)
    }

    suspend fun deleteStudent(student: Student){
        asystentDao.deleteStudent(student)
    }

    suspend fun deleteAllStudents(){
        asystentDao.deleteAllStudents()
    }

    // subject

    val allSubjects: Flow<List<Subject>> = asystentDao.getAllSubjects()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSubject(subject: Subject){
        asystentDao.insertSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject){
        asystentDao.deleteSubject(subject)
    }

    suspend fun deleteAllSubjects(){
        asystentDao.deleteAllSubjects()
    }

    suspend fun updateSubject(subject: Subject){
        asystentDao.updateSubject(subject)
    }

    // grade

    suspend fun insertGrade(grade: Grade){
        asystentDao.insertGrade(grade)
    }

    suspend fun deleteGrade(grade: Grade){
        asystentDao.deleteGrade(grade)
    }

    suspend fun updateGrade(grade: Grade){
        asystentDao.updateGrade(grade)
    }

    suspend fun deleteAllGrades(){
        asystentDao.deleteAllGrades()
    }

    fun getGradesOfStudentOfSubject(studentId: Int, subjectId: Int): Flow<List<Grade>>{
        return asystentDao.getGradesOfStudentOfSubject(studentId, subjectId)
    }

    suspend fun deleteGradesOfStudent(studentId: Int){
        asystentDao.deleteGradesOfStudent(studentId)
    }

    suspend fun deleteGradesOfSubject(subjectId: Int){
        asystentDao.deleteGradesOfSubject(subjectId)
    }

    // student - subject

    fun getStudentsOfSubject(subjectId: Int): List<SubjectWithStudents>{
        return asystentDao.getStudentsOfSubject(subjectId)
    }

    fun getSubjectsOfStudent(studentId: Int): List<StudentWithSubjects>{
        return asystentDao.getSubjectsOfStudent(studentId)
    }

    fun getSubjectsOfNotStudent(studentId: Int): Flow<List<Subject>>{
        return asystentDao.getSubjectsNotOfStudent(studentId)
    }

    suspend fun insertStudentSubjectCrossRef(studentSubjectCrossRef: StudentSubjectCrossRef){
        asystentDao.insertStudentSubjectCrossRef(studentSubjectCrossRef)
    }

    suspend fun deleteStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef){
        asystentDao.deleteStudentSubjectCrossRef(crossRef)
    }

    suspend fun deleteAllCrossRefs(){
        asystentDao.deleteAllCrossRefs()
    }

    suspend fun clearSqliteSequence(){
        asystentDao.clearSqliteSequence()
    }
}