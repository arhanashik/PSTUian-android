package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.User

interface FacultyRepository {
    suspend fun getFaculties(forceRefresh: Boolean = false): List<FacultyEntity>

    suspend fun getFaculty(id: Int): FacultyEntity

    suspend fun getBatches(facultyId: Int, forceRefresh: Boolean = false): List<BatchEntity>

    suspend fun getBatch(batchId: Int): BatchEntity

    suspend fun getStudents(
        facultyId: Int,
        batchId: Int,
        forceRefresh: Boolean = false
    ): List<User.Student>

    suspend fun getTeachers(facultyId: Int, forceRefresh: Boolean = false): List<User.Teacher>

    suspend fun getCourses(facultyId: Int, forceRefresh: Boolean = false): List<CourseEntity>

    suspend fun getEmployees(facultyId: Int, forceRefresh: Boolean = false): List<User.Employee>

    suspend fun getEmployeeProfile(userId: Int): EmployeeProfile

    suspend fun deleteAll()
}