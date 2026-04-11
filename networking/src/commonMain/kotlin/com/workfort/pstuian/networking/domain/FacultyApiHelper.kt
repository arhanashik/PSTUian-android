package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.model.dto.BatchDto
import com.workfort.pstuian.model.dto.CourseDto
import com.workfort.pstuian.model.dto.EmployeeDto
import com.workfort.pstuian.model.dto.FacultyDto
import com.workfort.pstuian.model.dto.StudentDto
import com.workfort.pstuian.model.dto.TeacherDto


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