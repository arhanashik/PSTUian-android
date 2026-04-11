package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.dto.BatchDto
import com.workfort.pstuian.model.dto.CourseDto
import com.workfort.pstuian.model.dto.EmployeeDto
import com.workfort.pstuian.model.dto.FacultyDto
import com.workfort.pstuian.model.Response
import com.workfort.pstuian.model.dto.StudentDto
import com.workfort.pstuian.model.dto.TeacherDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class FacultyApiService(private val client: HttpClient) {
    suspend fun getFaculties(): Response<List<FacultyDto>> {
        return client.get(NetworkConst.Remote.Api.Faculty.GET_ALL).body()
    }

    suspend fun getFaculty(id: Int): Response<FacultyDto> {
        return client.get(NetworkConst.Remote.Api.Faculty.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun getBatches(facultyId: Int): Response<List<BatchDto>> {
        return client.get(NetworkConst.Remote.Api.BATCH.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }

    suspend fun getBatch(id: Int): Response<BatchDto> {
        return client.get(NetworkConst.Remote.Api.BATCH.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun getStudents(facultyId: Int, batchId: Int): Response<List<StudentDto>> {
        return client.get(NetworkConst.Remote.Api.Student.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
            parameter(NetworkConst.Params.BATCH_ID, batchId)
        }.body()
    }

    suspend fun getTeachers(facultyId: Int): Response<List<TeacherDto>> {
        return client.get(NetworkConst.Remote.Api.Teacher.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }

    suspend fun getCourseSchedules(facultyId: Int): Response<List<CourseDto>> {
        return client.get(NetworkConst.Remote.Api.Course.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }

    suspend fun getEmployees(facultyId: Int): Response<List<EmployeeDto>> {
        return client.get(NetworkConst.Remote.Api.Employee.GET_ALL) {
            parameter(NetworkConst.Params.FACULTY_ID, facultyId)
        }.body()
    }
}
