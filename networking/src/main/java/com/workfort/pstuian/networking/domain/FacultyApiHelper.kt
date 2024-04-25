package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity


interface FacultyApiHelper {
    suspend fun getFaculties(): List<FacultyEntity>
    suspend fun getFaculty(id: Int): FacultyEntity
    suspend fun getBatches(facultyId: Int): List<BatchEntity>
    suspend fun getBatch(id: Int): BatchEntity
    suspend fun getStudents(facultyId: Int, batchId: Int): List<StudentEntity>
    suspend fun getTeachers(facultyId: Int): List<TeacherEntity>
    suspend fun getCourses(facultyId: Int): List<CourseEntity>
    suspend fun getEmployees(facultyId: Int): List<EmployeeEntity>
}