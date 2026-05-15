package com.workfort.pstuian.data.infrastructure.repository

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
) : FacultyRepository {
    private val facultiesCache = mutableSetOf<Faculty>()
    private val batchesCache = mutableMapOf<String, List<Batch>>()
    private val studentsCache = mutableMapOf<String, List<User.Student>>()
    private val teachersCache = mutableMapOf<String, List<User.Teacher>>()
    private val coursesCache = mutableMapOf<String, List<Course>>()
    private val employeesCache = mutableMapOf<String, List<User.Employee>>()

    override suspend fun getFaculties(forceRefresh: Boolean): DomainResult<List<Faculty>> {
        if (forceRefresh) facultiesCache.clear()

        if (facultiesCache.isNotEmpty()) return DomainResult.success(facultiesCache.toList())

        return helper.getFaculties()
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { faculties ->
                facultiesCache.clear()
                facultiesCache.addAll(faculties)
            }
    }

    override suspend fun getFaculty(id: Int): DomainResult<Faculty> {
        facultiesCache.firstOrNull { it.id == id }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.getFaculty(id)
            .toDomainResult()
            .map { it.toModel() }
            .onSuccess { facultiesCache.add(it) }
    }

    override suspend fun getBatches(
        facultyId: Int,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<Batch>> {
        if (forceRefresh) batchesCache.clear()

        val key = "$facultyId-$page"
        val cache = batchesCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getBatches(facultyId, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { batchesCache[key] = it }
    }

    override suspend fun getBatch(batchId: Int): DomainResult<Batch> {
        batchesCache.values.flatten().firstOrNull { it.id == batchId }?.let { batch ->
            return DomainResult.success(batch)
        }

        return helper.getBatch(batchId).toDomainResult().map { it.toModel() }
    }

    override suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<User.Student>> {
        if (forceRefresh) studentsCache.clear()

        val key = "$facultyId-$batchId-$page"
        val cache = studentsCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getStudents(facultyId, batchId, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { studentsCache[key] = it }
    }

    override suspend fun getTeachers(
        facultyId: Int,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<User.Teacher>> {
        if (forceRefresh) teachersCache.clear()

        val key = "$facultyId-$page"
        val cache = teachersCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getTeachers(facultyId, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { teachersCache[key] = it }
    }

    override suspend fun getCourses(
        facultyId: Int,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<Course>> {
        if (forceRefresh) coursesCache.clear()

        val key = "$facultyId-$page"
        val cache = coursesCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getCourses(facultyId, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { coursesCache[key] = it }
    }

    override suspend fun getEmployees(
        facultyId: Int,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<User.Employee>> {
        if (forceRefresh) employeesCache.clear()

        val key = "$facultyId-$page"
        val cache = employeesCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getEmployees(facultyId, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { employeesCache[key] = it }
    }

    override suspend fun getEmployee(id: Int): DomainResult<User.Employee> {
        employeesCache.values.flatten().firstOrNull { it.userId == id }?.let { batch ->
            return DomainResult.success(batch)
        }

        return DomainResult.failure(DomainError(DomainErrorCode.Auth.UserNotFound)) // TODO: Api call
    }

    override suspend fun clearCache() {
        facultiesCache.clear()
        batchesCache.clear()
        studentsCache.clear()
        teachersCache.clear()
        coursesCache.clear()
        employeesCache.clear()
    }
}
