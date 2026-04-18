package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository

class TeacherRepositoryImpl(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val helper: TeacherApiHelper,
) : TeacherRepository {
    private val teachers = mutableMapOf<Int, TeacherEntity>()

    override suspend fun getProfile(teacherId: Int): TeacherProfile {
        // get teacher
        var teacher = teachers[teacherId]
        if (teacher == null) {
            teacher = helper.get(teacherId).toEntity()
            teachers[teacherId] = teacher
        }
        // get faculty
        val faculty = facultyRepo.getFaculty(teacher.facultyId)
        // get sign in state
        val isSignedIn = try {
            val user = authRepo.getSignInUser()
            user is TeacherEntity && user.id == teacherId
        } catch (_: Exception) {
            false
        }

        return TeacherProfile(teacher, faculty, isSignedIn)
    }

    override suspend fun changeProfileImage(teacher: TeacherEntity, imageUrl: String): Boolean {
        val isChanged = helper.changeProfileImage(teacher.id, imageUrl)
        if (isChanged) {
            teacher.imageUrl = imageUrl
            authRepo.storeSignInTeacher(teacher)
            teachers[teacher.id] = teacher
        }
        return isChanged
    }

    override suspend fun changeName(teacher: TeacherEntity, name: String): Boolean {
        val isChanged = helper.changeName(teacher.id, name)
        if (isChanged) {
            teacher.name = name
            authRepo.storeSignInTeacher(teacher)
            teachers[teacher.id] = teacher
        }
        return isChanged
    }

    override suspend fun changeBio(teacher: TeacherEntity, bio: String): Boolean {
        val isChanged = helper.changeBio(teacher.id, bio)
        if (isChanged) {
            teacher.bio = bio
            authRepo.storeSignInTeacher(teacher)
            teachers[teacher.id] = teacher
        }
        return isChanged
    }

    override suspend fun changeAcademicInfo(
        teacher: TeacherEntity,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): TeacherEntity {
        helper.changeAcademicInfo(
            teacher.id, name, designation, department, blood, facultyId
        ).toEntity().let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teachers[updatedTeacher.id] = updatedTeacher
            return updatedTeacher
        }
    }

    override suspend fun changeConnectInfo(
        teacher: TeacherEntity,
        address: String,
        phone: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): TeacherEntity {
        val oldEmail = teacher.email ?: ""
        helper.changeConnectInfo(
            teacher.id, address, phone, email, oldEmail, linkedIn, fbLink
        ).toEntity().let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teachers[updatedTeacher.id] = updatedTeacher
            return updatedTeacher
        }
    }
}
