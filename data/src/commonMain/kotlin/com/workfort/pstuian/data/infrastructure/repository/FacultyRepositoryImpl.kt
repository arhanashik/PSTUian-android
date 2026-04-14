package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.local.database.service.BatchDbService
import com.workfort.pstuian.data.local.database.service.CourseDbService
import com.workfort.pstuian.data.local.database.service.EmployeeDbService
import com.workfort.pstuian.data.local.database.service.FacultyDbService
import com.workfort.pstuian.data.local.database.service.StudentDbService
import com.workfort.pstuian.data.local.database.service.TeacherDbService
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.EmployeeEntity
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.repository.FacultyRepository

class FacultyRepositoryImpl(
    private val facultyDbService: FacultyDbService,
    private val batchDbService: BatchDbService,
    private val studentDbService: StudentDbService,
    private val teacherDbService: TeacherDbService,
    private val courseDbService: CourseDbService,
    private val employeeDbService: EmployeeDbService,
    private val helper: FacultyApiHelper,
) : FacultyRepository {
    override suspend fun getFaculties(forceRefresh: Boolean): List<FacultyEntity> {
        val existingData = if (forceRefresh) emptyList() else facultyDbService.getAll()
        if (existingData.isEmpty()) {
            val newData = helper.getFaculties().map { it.toEntity() }
            facultyDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun getFaculty(id: Int): FacultyEntity {
        return facultyDbService.get(id) ?: return helper.getFaculty(id).toEntity()
    }

    override suspend fun getBatches(facultyId: Int, forceRefresh: Boolean): List<BatchEntity> {
        val existingData = if (forceRefresh) emptyList() else batchDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getBatches(facultyId).map { it.toEntity() }
            batchDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun getBatch(batchId: Int): BatchEntity {
        return batchDbService.get(batchId) ?: return helper.getBatch(batchId).toEntity()
    }

    override suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        forceRefresh: Boolean,
    ): List<StudentEntity> {
        val existingData =
            if (forceRefresh) emptyList() else studentDbService.getAll(facultyId, batchId)
        if (existingData.isEmpty()) {
            val newData = helper.getStudents(facultyId, batchId).map { it.toEntity() }
            studentDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun getTeachers(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<TeacherEntity> {
        val existingData = if (forceRefresh) emptyList() else teacherDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getTeachers(facultyId).map { it.toEntity() }
            teacherDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun getCourses(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<CourseEntity> {
        val existingData = if (forceRefresh) emptyList() else courseDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getCourses(facultyId).map { it.toEntity() }
            courseDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun getEmployees(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<EmployeeEntity> {
        val existingData = if (forceRefresh) emptyList() else employeeDbService.getAll(facultyId)
        if (existingData.isEmpty()) {
            val newData = helper.getEmployees(facultyId).map {
                it.copy(facultyId = facultyId).toEntity()
            }
            employeeDbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun getEmployeeProfile(userId: Int): EmployeeProfile {
        val employee = employeeDbService.get(userId)
        val facultyId = employee.facultyId
        val faculty = facultyDbService.get(facultyId) ?: helper.getFaculty(facultyId).toEntity()

        return EmployeeProfile(employee, faculty, isSignedIn = false)
    }

    override suspend fun deleteAll() {
        facultyDbService.deleteAll()
        batchDbService.deleteAll()
        studentDbService.deleteAll()
        teacherDbService.deleteAll()
        courseDbService.deleteAll()
        employeeDbService.deleteAll()
    }
}