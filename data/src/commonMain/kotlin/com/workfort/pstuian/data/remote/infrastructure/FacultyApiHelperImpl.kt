package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.BatchDto
import com.workfort.pstuian.data.model.CourseDto
import com.workfort.pstuian.data.model.EmployeeDto
import com.workfort.pstuian.data.model.FacultyDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.FacultyApiService

class FacultyApiHelperImpl(private val service: FacultyApiService) : FacultyApiHelper {

    override suspend fun getFaculties(): NetworkResult<List<FacultyDto>> {
        return safeApiCall { service.getFaculties() }
    }

    override suspend fun getFaculty(id: Int): NetworkResult<FacultyDto> {
        return safeApiCall { service.getFaculty(id) }
    }

    override suspend fun getBatches(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<BatchDto>> {
        return safeApiCall { service.getBatches(facultyId, page, limit) }
    }

    override suspend fun getBatch(id: Int): NetworkResult<BatchDto> {
        return safeApiCall { service.getBatch(id) }
    }

    override suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<StudentDto>> {
        return safeApiCall { service.getStudents(facultyId, batchId, page, limit) }
    }

    override suspend fun getTeachers(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<TeacherDto>> {
        return safeApiCall { service.getTeachers(facultyId, page, limit) }
    }

    override suspend fun getCourses(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CourseDto>> {
        return safeApiCall { service.getCourseSchedules(facultyId, page, limit) }
    }

    override suspend fun getEmployees(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<EmployeeDto>> {
        return safeApiCall { service.getEmployees(facultyId, page, limit) }
    }
}
