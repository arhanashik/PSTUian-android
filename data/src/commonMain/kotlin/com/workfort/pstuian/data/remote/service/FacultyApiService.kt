package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.data.model.BatchDto
import com.workfort.pstuian.data.model.CourseDto
import com.workfort.pstuian.data.model.EmployeeDto
import com.workfort.pstuian.data.model.FacultyDto
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.model.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class FacultyApiService(private val client: HttpClient) {

    suspend fun getFaculties(): ApiResponse<List<FacultyDto>> {
        return client.get(NetworkConst.Remote.Api.Faculty.GET_ALL).body()
    }

    suspend fun getFaculty(id: Int): ApiResponse<FacultyDto> {
        return client.get(NetworkConst.Remote.Api.Faculty.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun getBatches(facultyId: Int): ApiResponse<List<BatchDto>> {
        return client.get(NetworkConst.Remote.Api.BATCH.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }

    suspend fun getBatch(id: Int): ApiResponse<BatchDto> {
        return client.get(NetworkConst.Remote.Api.BATCH.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        page: Int,
        limit: Int,
    ): ApiResponse<List<StudentDto>> {
        return client.get(NetworkConst.Remote.Api.Student.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
            parameter(NetworkConst.Params.BATCH_ID, batchId)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun getTeachers(facultyId: Int): ApiResponse<List<TeacherDto>> {
        return client.get(NetworkConst.Remote.Api.Teacher.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }

    suspend fun getCourseSchedules(facultyId: Int): ApiResponse<List<CourseDto>> {
        return client.get(NetworkConst.Remote.Api.Course.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }

    suspend fun getEmployees(facultyId: Int): ApiResponse<List<EmployeeDto>> {
        return client.get(NetworkConst.Remote.Api.Employee.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }
}
