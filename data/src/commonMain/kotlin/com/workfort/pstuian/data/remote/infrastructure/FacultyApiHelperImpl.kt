package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.BatchDto
import com.workfort.pstuian.data.model.CourseDto
import com.workfort.pstuian.data.model.EmployeeDto
import com.workfort.pstuian.data.model.FacultyDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.data.remote.service.FacultyApiService

class FacultyApiHelperImpl(private val service: FacultyApiService) : FacultyApiHelper {

    override suspend fun getFaculties(): NetworkResult<List<FacultyDto>> {
        return service.getFaculties().toNetworkResult()
    }

    override suspend fun getFaculty(id: Int): FacultyDto {
        val response = service.getFaculty(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No DATA")
    }

    override suspend fun getBatches(facultyId: Int): NetworkResult<List<BatchDto>> {
        return service.getBatches(facultyId).toNetworkResult()
    }

    override suspend fun getBatch(id: Int): BatchDto {
        val response = service.getBatch(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No DATA")
    }

    override suspend fun getStudents(facultyId: Int, batchId: Int): List<StudentDto> {
        val response = service.getStudents(facultyId, batchId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getTeachers(facultyId: Int): List<TeacherDto> {
        val response = service.getTeachers(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getCourses(facultyId: Int): List<CourseDto> {
        val response = service.getCourseSchedules(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getEmployees(facultyId: Int): List<EmployeeDto> {
        val response = service.getEmployees(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}