package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BatchDto
import com.workfort.pstuian.data.model.CourseDto
import com.workfort.pstuian.data.model.EmployeeDto
import com.workfort.pstuian.data.model.FacultyDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.NetworkConst


interface FacultyApiHelper {
    suspend fun getFaculties(): NetworkResult<List<FacultyDto>>

    suspend fun getFaculty(id: Int): NetworkResult<FacultyDto>

    suspend fun getBatches(
        facultyId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<BatchDto>>

    suspend fun getBatch(id: Int): NetworkResult<BatchDto>

    suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<StudentDto>>

    suspend fun getTeachers(
        facultyId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<TeacherDto>>

    suspend fun getCourses(
        facultyId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CourseDto>>

    suspend fun getEmployees(
        facultyId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<EmployeeDto>>
}