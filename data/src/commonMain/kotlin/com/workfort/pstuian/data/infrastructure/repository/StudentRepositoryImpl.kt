package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.local.database.service.StudentDbService
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class StudentRepositoryImpl(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepositoryImpl,
    private val studentDbService: StudentDbService,
    private val helper: StudentApiHelper,
) : StudentRepository {
    override suspend fun getProfile(studentId: Int): StudentProfile {
        // get student
        var student = studentDbService.get(studentId)
        if (student == null) {
            student = helper.get(studentId).toEntity()
            studentDbService.insert(student)
        }
        // get faculty
        val faculty = facultyRepo.getFaculty(student.facultyId)
        // get batch
        val batch = facultyRepo.getBatch(student.batchId)
        // get sign in state
        val isSignedIn = try {
            val user = authRepo.getSignInUser()
            user is StudentEntity && user.id == studentId
        } catch (_: Exception) {
            false
        }

        return StudentProfile(student, faculty, batch, isSignedIn)
    }

    override suspend fun changeProfileImage(student: StudentEntity, imageUrl: String): Boolean {
        val isChanged = helper.changeProfileImage(student.id, imageUrl)
        if (isChanged) {
            student.imageUrl = imageUrl
            authRepo.storeSignInStudent(student)
            studentDbService.update(student)
        }
        return isChanged
    }

    override suspend fun changeName(student: StudentEntity, name: String): Boolean {
        val isChanged = helper.changeName(student.id, name)
        if (isChanged) {
            student.name = name
            authRepo.storeSignInStudent(student)
            studentDbService.update(student)
        }
        return isChanged
    }

    override suspend fun changeBio(student: StudentEntity, bio: String): Boolean {
        val isChanged = helper.changeBio(student.id, bio)
        if (isChanged) {
            student.bio = bio
            authRepo.storeSignInStudent(student)
            studentDbService.update(student)
        }
        return isChanged
    }

    override suspend fun changeAcademicInfo(
        student: StudentEntity,
        name: String,
        id: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): StudentEntity {
        helper.changeAcademicInfo(
            name, student.id, id, reg, blood, facultyId, session, batchId
        ).toEntity().let { updatedStudent ->
            authRepo.storeSignInStudent(updatedStudent)
            if (student.id != id) {
                studentDbService.delete(student)
                studentDbService.insert(updatedStudent)
            } else {
                studentDbService.update(updatedStudent)
            }
            return updatedStudent
        }
    }

    override suspend fun changeConnectInfo(
        student: StudentEntity,
        address: String,
        phone: String,
        email: String,
        cvLink: String,
        linkedIn: String,
        facebook: String
    ): StudentEntity {
        val oldEmail = student.email ?: ""
        helper.changeConnectInfo(
            student.id, address, phone, email, oldEmail, cvLink, linkedIn, facebook
        ).toEntity().let { updatedStudent ->
            authRepo.storeSignInStudent(updatedStudent)
            studentDbService.update(updatedStudent)
            return updatedStudent
        }
    }
}