package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.dto.BatchDto
import com.workfort.pstuian.data.dto.CourseDto
import com.workfort.pstuian.data.dto.EmployeeDto
import com.workfort.pstuian.data.dto.FacultyDto
import com.workfort.pstuian.data.dto.StudentDto
import com.workfort.pstuian.data.dto.TeacherDto


interface FacultyApiHelper {
    suspend fun getFaculties(): List<FacultyDto>
    suspend fun getFaculty(id: Int): FacultyDto
    suspend fun getBatches(facultyId: Int): List<BatchDto>
    suspend fun getBatch(id: Int): BatchDto
    suspend fun getStudents(facultyId: Int, batchId: Int): List<StudentDto>
    suspend fun getTeachers(facultyId: Int): List<TeacherDto>
    suspend fun getCourses(facultyId: Int): List<CourseDto>
    suspend fun getEmployees(facultyId: Int): List<EmployeeDto>
}