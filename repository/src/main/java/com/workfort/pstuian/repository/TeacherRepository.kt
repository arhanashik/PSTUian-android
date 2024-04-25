package com.workfort.pstuian.repository

import com.workfort.pstuian.database.service.TeacherDbService
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.model.TeacherProfile
import com.workfort.pstuian.networking.domain.TeacherApiHelper


class TeacherRepository(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val teacherDbService: TeacherDbService,
    private val helper: TeacherApiHelper,
) {
    suspend fun getProfile(teacherId: Int): TeacherProfile {
        // get teacher
        var teacher = teacherDbService.get(teacherId)
        if (teacher == null) {
            teacher = helper.get(teacherId)
            teacherDbService.insert(teacher)
        }
        // get faculty
        val faculty = facultyRepo.getFaculty(teacher.facultyId)
        // get sign in state
        val isSignedIn = try {
            val user = authRepo.getSignInUser()
            user is TeacherEntity && user.id == teacherId
        } catch (ex: Exception) {
            false
        }

        return TeacherProfile(teacher, faculty, isSignedIn)
    }

    suspend fun changeProfileImage(teacher: TeacherEntity, imageUrl: String): Boolean {
        val isChanged = helper.changeProfileImage(teacher.id, imageUrl)
        if (isChanged) {
            teacher.imageUrl = imageUrl
            authRepo.storeSignInTeacher(teacher)
            teacherDbService.update(teacher)
        }
        return isChanged
    }

    suspend fun changeName(teacher: TeacherEntity, name: String): Boolean {
        val isChanged = helper.changeName(teacher.id, name)
        if (isChanged) {
            teacher.name = name
            authRepo.storeSignInTeacher(teacher)
            teacherDbService.update(teacher)
        }
        return isChanged
    }

    suspend fun changeBio(teacher: TeacherEntity, bio: String): Boolean {
        val isChanged = helper.changeBio(teacher.id, bio)
        if (isChanged) {
            teacher.bio = bio
            authRepo.storeSignInTeacher(teacher)
            teacherDbService.update(teacher)
        }
        return isChanged
    }

    suspend fun changeAcademicInfo(
        teacher: TeacherEntity,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): TeacherEntity {
        helper.changeAcademicInfo(
            teacher.id, name, designation, department, blood, facultyId
        ).let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teacherDbService.update(updatedTeacher)
            return updatedTeacher
        }
    }

    suspend fun changeConnectInfo(
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
        ).let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teacherDbService.update(updatedTeacher)
            return updatedTeacher
        }
    }
}