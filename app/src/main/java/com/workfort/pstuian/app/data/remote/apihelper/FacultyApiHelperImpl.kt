package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.util.remote.FacultyApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 12:58 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */
class FacultyApiHelperImpl(private val service: FacultyApiService) : FacultyApiHelper {
    override suspend fun getFaculties(): List<FacultyEntity> {
        val response = service.getFaculties()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getBatches(facultyId: Int): List<BatchEntity> {
        val response = service.getBatches(facultyId)
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }

    override suspend fun getBatch(batchId: Int): BatchEntity {
        val response = service.getBatch(batchId)
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