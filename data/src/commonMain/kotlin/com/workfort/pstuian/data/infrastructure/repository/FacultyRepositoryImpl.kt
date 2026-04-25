package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.FacultyApiHelper
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
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
    private val coursesCache = mutableMapOf<Int, List<Course>>()
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

    override suspend fun getBatches(facultyId: Int, forceRefresh: Boolean): DomainResult<List<Batch>> {
        val cache = batchesCache[facultyId]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getBatches(facultyId)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { batchesCache[facultyId] = it }
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
    ): DomainResult<List<User.Student>> {
        val key = "${facultyId}_${batchId}"

        val cache = studentsCache[key]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getStudents(facultyId, batchId)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { studentsCache[key] = it }
    }

    override suspend fun getTeachers(facultyId: Int, forceRefresh: Boolean): DomainResult<List<User.Teacher>> {
        val cache = teachersCache[facultyId]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getTeachers(facultyId)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { teachersCache[facultyId] = it }
    }

    override suspend fun getCourses(facultyId: Int, forceRefresh: Boolean): DomainResult<List<Course>> {
        val cache = coursesCache[facultyId]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getCourses(facultyId)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { coursesCache[facultyId] = it }
    }

    override suspend fun getEmployees(facultyId: Int, forceRefresh: Boolean): DomainResult<List<User.Employee>> {
        val cache = employeesCache[facultyId]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getEmployees(facultyId)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { employeesCache[facultyId] = it }
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
