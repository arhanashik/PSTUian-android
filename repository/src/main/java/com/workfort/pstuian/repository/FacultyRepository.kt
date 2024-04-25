package com.workfort.pstuian.repository

import com.workfort.pstuian.database.service.BatchDbService
import com.workfort.pstuian.database.service.CourseDbService
import com.workfort.pstuian.database.service.EmployeeDbService
import com.workfort.pstuian.database.service.FacultyDbService
import com.workfort.pstuian.database.service.StudentDbService
import com.workfort.pstuian.database.service.TeacherDbService
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.EmployeeProfile
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.networking.domain.FacultyApiHelper

class FacultyRepository(
    private val facultyDbService: FacultyDbService,
    private val batchDbService: BatchDbService,
    private val studentDbService: StudentDbService,
    private val teacherDbService: TeacherDbService,
    private val courseDbService: CourseDbService,
    private val employeeDbService: EmployeeDbService,
    private val helper: FacultyApiHelper,
) {
    suspend fun getFaculties(forceRefresh: Boolean = false): List<FacultyEntity> {
        val existingData = if (forceRefresh) emptyList() else facultyDbService.getAll()
        if (existingData.isEmpty()) {
            val newData = helper.getFaculties()
            facultyDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getFaculty(id: Int): FacultyEntity {
        return facultyDbService.get(id) ?: return helper.getFaculty(id)
    }

    suspend fun getBatches(facultyId: Int, forceRefresh: Boolean = false): List<BatchEntity> {
        val existingData = if (forceRefresh) emptyList() else batchDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
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
    ): List<StudentEntity> {
        val existingData =
            if (forceRefresh) emptyList() else studentDbService.getAll(facultyId, batchId)
        if (existingData.isEmpty()) {
            val newData = helper.getStudents(facultyId, batchId)
            studentDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getTeachers(facultyId: Int, forceRefresh: Boolean = false): List<TeacherEntity> {
        val existingData = if (forceRefresh) emptyList() else teacherDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getTeachers(facultyId)
            teacherDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getCourses(facultyId: Int, forceRefresh: Boolean = false): List<CourseEntity> {
        val existingData = if (forceRefresh) emptyList() else courseDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getCourses(facultyId)
            courseDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getEmployees(facultyId: Int, forceRefresh: Boolean = false): List<EmployeeEntity> {
        val existingData = if (forceRefresh) emptyList() else employeeDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getEmployees(facultyId).map {
                it.copy(facultyId = facultyId)
            }
            employeeDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun getEmployeeProfile(userId: Int): EmployeeProfile {
        val employee = employeeDbService.get(userId)
        val facultyId = employee.facultyId
        val faculty = facultyDbService.get(facultyId) ?: helper.getFaculty(facultyId)

        return EmployeeProfile(employee, faculty)
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