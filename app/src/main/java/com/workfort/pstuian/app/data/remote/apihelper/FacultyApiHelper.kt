package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 5:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/2/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface FacultyApiHelper {
    suspend fun getFaculties(): List<FacultyEntity>
    suspend fun getBatches(facultyId: Int): List<BatchEntity>
    suspend fun getBatch(batchId: Int): BatchEntity
    suspend fun getStudents(facultyId: Int, batchId: Int): List<StudentEntity>
    suspend fun getTeachers(facultyId: Int): List<TeacherEntity>
    suspend fun getCourses(facultyId: Int): List<CourseEntity>
    suspend fun getEmployees(facultyId: Int): List<EmployeeEntity>
}