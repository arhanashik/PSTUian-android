package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherService
import com.workfort.pstuian.app.data.remote.apihelper.TeacherApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Nov, 2021 at 1:17 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 11/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class TeacherRepository(
    private val authRepo: AuthRepository,
    private val teacherDbService: TeacherService,
    private val helper: TeacherApiHelper
) {
    suspend fun changeProfileImage(teacher: TeacherEntity, imageUrl: String): Boolean {
        val isChanged = helper.changeProfileImage(teacher.id, imageUrl)
        if(isChanged) {
            teacher.imageUrl = imageUrl
            authRepo.storeSignInTeacher(teacher)
            teacherDbService.update(teacher)
        }
        return isChanged
    }

    suspend fun changeName(teacher: TeacherEntity, name: String): Boolean {
        val isChanged = helper.changeName(teacher.id, name)
        if(isChanged) {
            teacher.name = name
            authRepo.storeSignInTeacher(teacher)
            teacherDbService.update(teacher)
        }
        return isChanged
    }

    suspend fun changeBio(teacher: TeacherEntity, bio: String): Boolean {
        val isChanged = helper.changeBio(teacher.id, bio)
        if(isChanged) {
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
        val oldEmail = teacher.email?: ""
        helper.changeConnectInfo(
            teacher.id, address, phone, email, oldEmail, linkedIn, fbLink
        ).let { updatedTeacher ->
            authRepo.storeSignInTeacher(updatedTeacher)
            teacherDbService.update(updatedTeacher)
            return updatedTeacher
        }
    }
}