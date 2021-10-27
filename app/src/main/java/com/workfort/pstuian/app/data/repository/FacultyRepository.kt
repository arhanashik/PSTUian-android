package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.batch.BatchService
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.course.CourseService
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeService
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyService
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.student.StudentService
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherService
import com.workfort.pstuian.app.data.remote.apihelper.FacultyApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:40 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class FacultyRepository(
    private val facultyDbService: FacultyService,
    private val batchDbService: BatchService,
    private val studentDbService: StudentService,
    private val teacherDbService: TeacherService,
    private val courseDbService: CourseService,
    private val employeeDbService: EmployeeService,
    private val helper: FacultyApiHelper
) {
    suspend fun getFaculties() : List<FacultyEntity> {
        val existingData = facultyDbService.getAll()
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getFaculties()
            facultyDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getBatches(facultyId: Int) : List<BatchEntity> {
        val existingData = batchDbService.getAll(facultyId)
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getBatches(facultyId)
            batchDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getBatch(batchId: Int) : BatchEntity {
        val existingData = batchDbService.get(batchId)
        if(existingData == null) {
            val newData = helper.getBatch(batchId)
//            batchDbService.insert(newData)
            return newData
        }

        return existingData
    }

    suspend fun updateStudent(student: StudentEntity) {
        studentDbService.update(student)
    }

    suspend fun getStudents(facultyId: Int, batchId: Int) : List<StudentEntity> {
        val existingData = studentDbService.getAll(facultyId, batchId)
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getStudents(facultyId, batchId)
            studentDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getTeachers(facultyId: Int) : List<TeacherEntity> {
        val existingData = teacherDbService.getAll(facultyId)
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getTeachers(facultyId)
            teacherDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getCourses(facultyId: Int) : List<CourseEntity> {
        val existingData = courseDbService.getAll(facultyId)
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getCourses(facultyId)
            courseDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getEmployees(facultyId: Int) : List<EmployeeEntity> {
        val existingData = employeeDbService.getAll(facultyId)
        if(existingData.isNullOrEmpty()) {
            val newData = helper.getEmployees(facultyId)
            employeeDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun deleteAll() {
        facultyDbService.deleteAll()
        batchDbService.deleteAll()
        studentDbService.deleteAll()
        teacherDbService.deleteAll()
        courseDbService.deleteAll()
        employeeDbService.deleteAll()
    }
}