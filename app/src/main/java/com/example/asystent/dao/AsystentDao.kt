package com.example.asystent.dao

import androidx.room.*
import com.example.asystent.entity.Grade
import com.example.asystent.entity.Student
import com.example.asystent.entity.Subject
import com.example.asystent.entity.relations.StudentSubjectCrossRef
import com.example.asystent.entity.relations.StudentWithSubjects
import com.example.asystent.entity.relations.SubjectWithStudents
import kotlinx.coroutines.flow.Flow

@Dao
interface AsystentDao {

    // student

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student): Long

    @Query("SELECT * FROM student_table ORDER BY firstName, lastName ASC")
    fun getAllStudents(): Flow<List<Student>>

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM student_table")
    suspend fun deleteAllStudents()

    // subject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject)

    @Query("SELECT * FROM subject_table ORDER BY weekDayNumber, timeFrom, name ASC")
    fun getAllSubjects(): Flow<List<Subject>>

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Update
    suspend fun updateSubject(subject: Subject)

    @Query("DELETE FROM subject_table")
    suspend fun deleteAllSubjects()

    // grade

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)

    @Update
    suspend fun updateGrade(grade: Grade)

    @Query("DELETE FROM grade_table")
    suspend fun deleteAllGrades()

    @Query("DELETE FROM grade_table WHERE studentId = :studentId")
    suspend fun deleteGradesOfStudent(studentId: Int)

    @Query("DELETE FROM grade_table WHERE subjectId = :subjectId")
    suspend fun deleteGradesOfSubject(subjectId: Int)

    // student - subject

    @Query("SELECT * FROM StudentSubjectCrossRef")
    fun getAllCrossRefs(): Flow<List<StudentSubjectCrossRef>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef)

    @Transaction
    @Query("SELECT * FROM subject_table WHERE subjectId = :subjectId")
    fun getStudentsOfSubject(subjectId: Int): List<SubjectWithStudents>

    @Transaction
    @Query("SELECT * FROM student_table WHERE studentId = :studentId")
    fun getSubjectsOfStudent(studentId: Int): List<StudentWithSubjects>

    @Transaction
    @Query("SELECT * FROM subject_table " +
            "WHERE subjectId NOT IN (" +
            "SELECT su.subjectId FROM StudentSubjectCrossRef AS ref, subject_table AS su " +
            "WHERE ref.studentId = :studentId and ref.subjectId = su.subjectId)")
    fun getSubjectsNotOfStudent(studentId: Int): Flow<List<Subject>>

    @Query("DELETE FROM StudentSubjectCrossRef")
    suspend fun deleteAllCrossRefs()

    @Delete
    suspend fun deleteStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef)

    // student - grade - subject

    @Query("SELECT * FROM grade_table WHERE studentId = :studentId AND subjectId = :subjectId")
    fun getGradesOfStudentOfSubject(studentId: Int, subjectId: Int): Flow<List<Grade>>

    // clear ids
    @Query("DELETE FROM sqlite_sequence")
    suspend fun clearSqliteSequence()

}