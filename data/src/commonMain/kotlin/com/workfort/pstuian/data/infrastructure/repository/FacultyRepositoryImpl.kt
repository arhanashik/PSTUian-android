package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.FacultyRepository

class FacultyRepositoryImpl(
    private val helper: FacultyApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : FacultyRepository {
    private val faculties = mutableListOf<FacultyEntity>()
    private val batches = mutableMapOf<Int, List<BatchEntity>>()
    private val students = mutableMapOf<String, List<User.Student>>()
    private val teachers = mutableMapOf<Int, List<User.Teacher>>()
    private val courses = mutableMapOf<Int, List<CourseEntity>>()
    private val employees = mutableMapOf<Int, List<User.Employee>>()

    override suspend fun getFaculties(forceRefresh: Boolean): List<FacultyEntity> {
        if (forceRefresh || faculties.isEmpty()) {
            helper.getFaculties().toDomainResult(domainErrorMapper).map { dtos ->
                val newData = dtos.map { it.toEntity() }
                faculties.clear()
                faculties.addAll(newData)
            }
        }
        return faculties
    }

    override suspend fun getFaculty(id: Int): FacultyEntity {
        return faculties.find { it.id == id } ?: helper.getFaculty(id).toEntity()
    }

    override suspend fun getBatches(facultyId: Int, forceRefresh: Boolean): List<BatchEntity> {
        if (forceRefresh || batches[facultyId].isNullOrEmpty()) {
            helper.getBatches(facultyId).toDomainResult(domainErrorMapper).map { dtos ->
                val newData = dtos.map { it.toEntity() }
                batches[facultyId] = newData
            }
        }
        return batches[facultyId] ?: emptyList()
    }

    override suspend fun getBatch(batchId: Int): BatchEntity {
        return batches.values.flatten().find { it.id == batchId }
            ?: helper.getBatch(batchId).toEntity()
    }

    override suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        forceRefresh: Boolean,
    ): List<User.Student> {
        val key = "${facultyId}_${batchId}"
        if (forceRefresh || !students.containsKey(key)) {
            val newData = helper.getStudents(facultyId, batchId).map { it.toModel() }
            students[key] = newData
        }
        return students[key] ?: emptyList()
    }

    override suspend fun getTeachers(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<User.Teacher> {
        if (forceRefresh || !teachers.containsKey(facultyId)) {
            val newData = helper.getTeachers(facultyId).map { it.toModel() }
            teachers[facultyId] = newData
        }
        return teachers[facultyId] ?: emptyList()
    }

    override suspend fun getCourses(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<CourseEntity> {
        if (forceRefresh || !courses.containsKey(facultyId)) {
            val newData = helper.getCourses(facultyId).map { it.toEntity() }
            courses[facultyId] = newData
        }
        return courses[facultyId] ?: emptyList()
    }

    override suspend fun getEmployees(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<User.Employee> {
        if (forceRefresh || !employees.containsKey(facultyId)) {
            val newData = helper.getEmployees(facultyId).map {
                it.copy(facultyId = facultyId).toModel()
            }
            employees[facultyId] = newData
        }
        return employees[facultyId] ?: emptyList()
    }

    override suspend fun getEmployeeProfile(userId: Int): EmployeeProfile {
        val employee = employees.values.flatten().find { it.userId == userId.toString() }
            ?: throw Exception("Profile not found")
            
        val facultyId = employee.facultyId
        val faculty = getFaculty(facultyId)

        return EmployeeProfile(employee, faculty, isSignedIn = false)
    }

    override suspend fun deleteAll() {
        faculties.clear()
        batches.clear()
        students.clear()
        teachers.clear()
        courses.clear()
        employees.clear()
    }
}
