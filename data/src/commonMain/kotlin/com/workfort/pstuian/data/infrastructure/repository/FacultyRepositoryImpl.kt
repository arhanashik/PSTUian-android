package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository

class FacultyRepositoryImpl(
    private val helper: FacultyApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : FacultyRepository {
    private val facultiesCache = mutableSetOf<Faculty>()
    private val batchesCache = mutableMapOf<Int, List<Batch>>()
    private val studentsCache = mutableMapOf<String, List<User.Student>>()
    private val teachersCache = mutableMapOf<Int, List<User.Teacher>>()
    private val coursesCache = mutableMapOf<Int, List<CourseEntity>>()
    private val employeesCache = mutableMapOf<Int, List<User.Employee>>()

    override suspend fun getFaculties(forceRefresh: Boolean): List<Faculty> {
        if (forceRefresh || facultiesCache.isEmpty()) {
            helper.getFaculties()
                .toDomainResult(domainErrorMapper)
                .map { dtos -> dtos.map { it.toModel() } }
                .onSuccess { faculties ->
                    facultiesCache.clear()
                    facultiesCache.addAll(faculties)
                }
        }
        return facultiesCache.toMutableList()
    }

    override suspend fun getFaculty(id: Int): DomainResult<Faculty> {
        facultiesCache.firstOrNull { it.id == id }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.getFaculty(id)
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { facultiesCache.add(it) }
    }

    override suspend fun getBatches(facultyId: Int, forceRefresh: Boolean): List<Batch> {
        if (forceRefresh || batchesCache[facultyId].isNullOrEmpty()) {
            helper.getBatches(facultyId)
                .toDomainResult(domainErrorMapper)
                .map { dtos -> dtos.map { it.toModel() } }
                .onSuccess { batchesCache[facultyId] = it }
        }
        return batchesCache[facultyId] ?: emptyList()
    }

    override suspend fun getBatch(batchId: Int): DomainResult<Batch> {
        batchesCache.values.flatten().firstOrNull { it.id == batchId }?.let { batch ->
            return DomainResult.success(batch)
        }

        return helper.getBatch(batchId).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        forceRefresh: Boolean,
    ): List<User.Student> {
        val key = "${facultyId}_${batchId}"
        if (forceRefresh || !studentsCache.containsKey(key)) {
            helper.getStudents(facultyId, batchId)
                .toDomainResult(domainErrorMapper)
                .map { dtos -> dtos.map { it.toModel() } }
                .onSuccess { studentsCache[key] = it }
        }
        return studentsCache[key] ?: emptyList()
    }

    override suspend fun getTeachers(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<User.Teacher> {
        if (forceRefresh || !teachersCache.containsKey(facultyId)) {
            helper.getTeachers(facultyId)
                .toDomainResult(domainErrorMapper)
                .map { dtos -> dtos.map { it.toModel() } }
                .onSuccess { teachersCache[facultyId] = it }
        }
        return teachersCache[facultyId] ?: emptyList()
    }

    override suspend fun getCourses(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<CourseEntity> {
        if (forceRefresh || !coursesCache.containsKey(facultyId)) {
            helper.getCourses(facultyId)
                .toDomainResult(domainErrorMapper)
                .map { dtos -> dtos.map { it.toModel() } }
                .onSuccess { coursesCache[facultyId] = it }
        }
        return coursesCache[facultyId] ?: emptyList()
    }

    override suspend fun getEmployees(
        facultyId: Int,
        forceRefresh: Boolean,
    ): List<User.Employee> {
        if (forceRefresh || !employeesCache.containsKey(facultyId)) {
            helper.getEmployees(facultyId)
                .toDomainResult(domainErrorMapper)
                .map { dtos -> dtos.map { it.toModel() } }
                .onSuccess { employeesCache[facultyId] = it }
        }
        return employeesCache[facultyId] ?: emptyList()
    }

    override suspend fun getEmployee(id: Int): DomainResult<User.Employee> {
        employeesCache.values.flatten().firstOrNull { it.id == id }?.let { batch ->
            return DomainResult.success(batch)
        }

        return DomainResult.failure(DomainError(DomainErrorCode.Auth.UserNotFound)) // TODO: Api call
    }

    override suspend fun deleteAll() {
        facultiesCache.clear()
        batchesCache.clear()
        studentsCache.clear()
        teachersCache.clear()
        coursesCache.clear()
        employeesCache.clear()
    }
}
