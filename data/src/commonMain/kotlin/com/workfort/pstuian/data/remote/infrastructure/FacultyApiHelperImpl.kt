package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.BatchDto
import com.workfort.pstuian.data.model.CommonNetworkError
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
        return runCatching {
            service.getFaculties().toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getFaculty(id: Int): NetworkResult<FacultyDto> {
        return runCatching {
            service.getFaculty(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getBatches(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<BatchDto>> {
        return runCatching {
            service.getBatches(facultyId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getBatch(id: Int): NetworkResult<BatchDto> {
        return runCatching {
            service.getBatch(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<StudentDto>> {
        return runCatching {
            service.getStudents(facultyId, batchId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getTeachers(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<TeacherDto>> {
        return runCatching {
            service.getTeachers(facultyId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getCourses(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CourseDto>> {
        return runCatching {
            service.getCourseSchedules(facultyId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getEmployees(
        facultyId: Int,
        page: Int,
        limit: Int,
    ): NetworkResult<List<EmployeeDto>> {
        return runCatching {
            service.getEmployees(facultyId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}