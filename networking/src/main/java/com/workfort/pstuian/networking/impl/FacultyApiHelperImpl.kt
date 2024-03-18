package com.workfort.pstuian.networking.impl

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
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
    com.workfort.pstuian.networking.FacultyApiHelper {
    override suspend fun getFaculties(): List<FacultyEntity> {
        val response = service.getFaculties()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getFaculty(id: Int): FacultyEntity {
        val response = service.getFaculty(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No DATA")
    }

    override suspend fun getBatches(facultyId: Int): List<BatchEntity> {
        val response = service.getBatches(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getBatch(id: Int): BatchEntity {
        val response = service.getBatch(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No DATA")
    }

    override suspend fun getStudents(facultyId: Int, batchId: Int): List<StudentEntity> {
        val response = service.getStudents(facultyId, batchId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getTeachers(facultyId: Int): List<TeacherEntity> {
        val response = service.getTeachers(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getCourses(facultyId: Int): List<CourseEntity> {
        val response = service.getCourseSchedules(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getEmployees(facultyId: Int): List<EmployeeEntity> {
        val response = service.getEmployees(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}