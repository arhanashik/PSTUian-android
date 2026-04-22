package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.AuthUser
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType

interface AuthRepository {
    fun getAuthUser(): AuthUser?
    fun isUserSignedIn(): Boolean
    fun isUserEmailVerified(): Boolean
    fun getSignInUserType(): UserType?
    suspend fun storeSignInTeacher(teacher: TeacherEntity)
    suspend fun signIn(email: String, password: String, userType: UserType): User
    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        password: String,
    ): User.Student

    suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
    ): TeacherEntity

    suspend fun signOut(fromAllDevice: Boolean = false): String
    suspend fun changePassword(oldPassword: String, newPassword: String): String
    suspend fun resetPassword(email: String): DomainResult<Unit>
    suspend fun sendVerificationEmail(email: String, password: String): DomainResult<Unit>
    suspend fun deleteAll()
    suspend fun deleteAccount(password: String): String
}