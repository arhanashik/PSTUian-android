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
    suspend fun getFaculty(id: Int): NetworkResult<FacultyDto>
    suspend fun getBatches(facultyId: Int): NetworkResult<List<BatchDto>>
    suspend fun getBatch(id: Int): NetworkResult<BatchDto>
    suspend fun getStudents(facultyId: Int, batchId: Int): NetworkResult<List<StudentDto>>
    suspend fun getTeachers(facultyId: Int): NetworkResult<List<TeacherDto>>
    suspend fun getCourses(facultyId: Int): NetworkResult<List<CourseDto>>
    suspend fun getEmployees(facultyId: Int): NetworkResult<List<EmployeeDto>>
}