package com.workfort.pstuian.repository

import com.workfort.pstuian.database.service.BatchService
import com.workfort.pstuian.database.service.CourseService
import com.workfort.pstuian.database.service.EmployeeService
import com.workfort.pstuian.database.service.FacultyService
import com.workfort.pstuian.database.service.StudentService
import com.workfort.pstuian.database.service.TeacherService
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.networking.FacultyApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:40 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  ****************************************************************************
 */

class FacultyRepository(
    private val facultyDbService: FacultyService,
    private val batchDbService: BatchService,
    private val studentDbService: StudentService,
    private val teacherDbService: TeacherService,
    private val courseDbService: CourseService,
    private val employeeDbService: EmployeeService,
    private val helper: com.workfort.pstuian.networking.FacultyApiHelper,
) {
    suspend fun getFaculties(forceRefresh: Boolean = false) : List<FacultyEntity> {
        val existingData = if(forceRefresh) emptyList() else facultyDbService.getAll()
        if(existingData.isEmpty()) {
            val newData = helper.getFaculties()
            facultyDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getFaculty(id: Int) : FacultyEntity {
        return facultyDbService.get(id) ?: return helper.getFaculty(id)
    }

    suspend fun getBatches(facultyId: Int, forceRefresh: Boolean = false) : List<BatchEntity> {
        val existingData = if(forceRefresh) emptyList() else batchDbService.getAll(facultyId)
        if(existingData.isEmpty()) {
            val newData = helper.getBatches(facultyId)
            batchDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getBatch(batchId: Int): BatchEntity {
        return batchDbService.get(batchId) ?: return helper.getBatch(batchId)
    }

    suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        forceRefresh: Boolean = false
    ) : List<StudentEntity> {
        val existingData = if(forceRefresh) emptyList() else studentDbService.getAll(facultyId, batchId)
        if(existingData.isEmpty()) {
            val newData = helper.getStudents(facultyId, batchId)
            studentDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getTeachers(facultyId: Int, forceRefresh: Boolean = false) : List<TeacherEntity> {
        val existingData = if(forceRefresh) emptyList() else teacherDbService.getAll(facultyId)
        if(existingData.isEmpty()) {
            val newData = helper.getTeachers(facultyId)
            teacherDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getCourses(facultyId: Int, forceRefresh: Boolean = false) : List<CourseEntity> {
        val existingData = if(forceRefresh) emptyList() else courseDbService.getAll(facultyId)
        if(existingData.isEmpty()) {
            val newData = helper.getCourses(facultyId)
            courseDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getEmployees(facultyId: Int, forceRefresh: Boolean = false) : List<EmployeeEntity> {
        val existingData = if(forceRefresh) emptyList() else employeeDbService.getAll(facultyId)
        if(existingData.isEmpty()) {
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