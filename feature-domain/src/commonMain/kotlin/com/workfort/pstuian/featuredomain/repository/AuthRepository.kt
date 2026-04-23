package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.AuthUser
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType

interface AuthRepository {
    fun getAuthUser(): AuthUser?
    fun isUserSignedIn(): Boolean
    fun isUserEmailVerified(): Boolean
    suspend fun signIn(email: String, password: String, userType: UserType): DomainResult<User>
    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        password: String,
    ): DomainResult<User.Student>

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        password: String,
    ): DomainResult<User.Teacher>

    suspend fun signOut(userType: UserType, fromAllDevice: Boolean = false): DomainResult<Unit>

    suspend fun changePassword(
        userType: UserType,
        oldPassword: String,
        newPassword: String,
    ): DomainResult<Unit>
    suspend fun resetPassword(email: String): DomainResult<Unit>
    suspend fun sendVerificationEmail(email: String, password: String): DomainResult<Unit>

    suspend fun deleteAccount(userType: UserType, password: String): DomainResult<Unit>
    suspend fun removeAuthPrefs()
}