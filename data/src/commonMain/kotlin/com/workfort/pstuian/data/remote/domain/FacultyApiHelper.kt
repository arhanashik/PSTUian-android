package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BatchDto
import com.workfort.pstuian.data.model.CourseDto
import com.workfort.pstuian.data.model.EmployeeDto
import com.workfort.pstuian.data.model.FacultyDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto


interface FacultyApiHelper {
    suspend fun getFaculties(): NetworkResult<List<FacultyDto>>
    suspend fun getFaculty(id: Int): FacultyDto
    suspend fun getBatches(facultyId: Int): List<BatchDto>
    suspend fun getBatch(id: Int): BatchDto
    suspend fun getStudents(facultyId: Int, batchId: Int): List<StudentDto>
    suspend fun getTeachers(facultyId: Int): List<TeacherDto>
    suspend fun getCourses(facultyId: Int): List<CourseDto>
    suspend fun getEmployees(facultyId: Int): List<EmployeeDto>
}