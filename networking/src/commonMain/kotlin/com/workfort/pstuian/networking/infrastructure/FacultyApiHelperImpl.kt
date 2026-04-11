package com.workfort.pstuian.networking.infrastructure

import com.workfort.pstuian.model.dto.BatchDto
import com.workfort.pstuian.model.dto.CourseDto
import com.workfort.pstuian.model.dto.EmployeeDto
import com.workfort.pstuian.model.dto.FacultyDto
import com.workfort.pstuian.model.dto.StudentDto
import com.workfort.pstuian.model.dto.TeacherDto
import com.workfort.pstuian.networking.domain.FacultyApiHelper
import com.workfort.pstuian.networking.service.FacultyApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 12:58 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */
class FacultyApiHelperImpl(private val service: FacultyApiService) :
    FacultyApiHelper {
    override suspend fun getFaculties(): List<FacultyDto> {
        val response = service.getFaculties()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getFaculty(id: Int): FacultyDto {
        val response = service.getFaculty(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No DATA")
    }

    override suspend fun getBatches(facultyId: Int): List<BatchDto> {
        val response = service.getBatches(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
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