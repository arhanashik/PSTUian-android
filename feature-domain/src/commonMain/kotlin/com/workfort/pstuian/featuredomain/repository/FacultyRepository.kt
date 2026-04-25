package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.User

interface FacultyRepository {
    suspend fun getFaculties(forceRefresh: Boolean = false): DomainResult<List<Faculty>>

    suspend fun getFaculty(id: Int): DomainResult<Faculty>

    suspend fun getBatches(facultyId: Int, forceRefresh: Boolean = false): DomainResult<List<Batch>>

    suspend fun getBatch(batchId: Int): DomainResult<Batch>

    suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        page: Int = 1,
        useCache: Boolean = true,
    ): DomainResult<List<User.Student>>

    suspend fun getTeachers(facultyId: Int, forceRefresh: Boolean = false): DomainResult<List<User.Teacher>>

    suspend fun getCourses(facultyId: Int, forceRefresh: Boolean = false): DomainResult<List<Course>>

    suspend fun getEmployees(facultyId: Int, forceRefresh: Boolean = false): DomainResult<List<User.Employee>>

    suspend fun getEmployee(id: Int): DomainResult<User.Employee>

    suspend fun clearCache()
}