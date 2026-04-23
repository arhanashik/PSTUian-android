package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository

class TeacherRepositoryImpl(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val helper: TeacherApiHelper,
) : TeacherRepository {
    private val teachers = mutableMapOf<Int, User.Teacher>()

    override suspend fun getProfile(teacherId: Int): TeacherProfile {
        // get teacher
        var teacher = teachers[teacherId]
        if (teacher == null) {
            teacher = helper.get(teacherId).toModel()
            teachers[teacherId] = teacher
        }
        // get faculty
        val faculty = facultyRepo.getFaculty(teacher.facultyId)
        // get sign in state
        val isSignedIn = authRepo.getAuthUser()?.userId == teacherId.toString()

        return TeacherProfile(teacher, faculty, isSignedIn)
    }

    override suspend fun changeProfileImage(teacher: User.Teacher, imageUrl: String): Boolean {
        val id = teacher.userId.toInt()
        val isChanged = helper.changeProfileImage(id, imageUrl)
        if (isChanged) {
            val updated = teacher.copy(imageUrl = imageUrl)
            authRepo.storeSignInTeacher(updated)
            teachers[id] = updated
        }
        return isChanged
    }

    override suspend fun changeName(teacher: User.Teacher, name: String): Boolean {
        val id = teacher.userId.toInt()
        val isChanged = helper.changeName(id, name)
        if (isChanged) {
            val updated = teacher.copy(name = name)
            authRepo.storeSignInTeacher(updated)
            teachers[id] = updated
        }
        return isChanged
    }

    override suspend fun changeBio(teacher: User.Teacher, bio: String): Boolean {
        val id = teacher.userId.toInt()
        val isChanged = helper.changeBio(id, bio)
        if (isChanged) {
            val updated = teacher.copy(bio = bio)
            authRepo.storeSignInTeacher(updated)
            teachers[id] = updated
        }
        return isChanged
    }

    override suspend fun changeAcademicInfo(
        teacher: User.Teacher,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): User.Teacher {
        helper.changeAcademicInfo(
            teacher.userId.toInt(), name, designation, department, blood, facultyId
        ).toModel().let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teachers[updatedTeacher.userId.toInt()] = updatedTeacher
            return updatedTeacher
        }
    }

    override suspend fun changeConnectInfo(
        teacher: User.Teacher,
        address: String,
        phone: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): User.Teacher {
        val oldEmail = teacher.email
        helper.changeConnectInfo(
            teacher.userId.toInt(), address, phone, email, oldEmail, linkedIn, fbLink
        ).toModel().let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teachers[updatedTeacher.userId.toInt()] = updatedTeacher
            return updatedTeacher
        }
    }
}
