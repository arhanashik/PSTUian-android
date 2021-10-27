package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.remote.apihelper.StudentApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 4:03 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class StudentRepository(
    private val authRepo: AuthRepository,
    private val helper: StudentApiHelper
) {
    suspend fun changeProfileImage(student: StudentEntity, imageUrl: String): Boolean {
        val isChanged = helper.changeProfileImage(student.id, imageUrl)
        if(isChanged) {
            student.imageUrl = imageUrl
            authRepo.storeSignInUser(student)
        }
        return isChanged
    }

    suspend fun changeName(student: StudentEntity, name: String): Boolean {
        val isChanged = helper.changeName(student.id, name)
        if(isChanged) {
            student.name = name
            authRepo.storeSignInUser(student)
        }
        return isChanged
    }

    suspend fun changeBio(student: StudentEntity, bio: String): Boolean {
        val isChanged = helper.changeBio(student.id, bio)
        if(isChanged) {
            student.bio = bio
            authRepo.storeSignInUser(student)
        }
        return isChanged
    }

    suspend fun changeAcademicInfo(
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
        ).let { updatedStudent ->
            authRepo.storeSignInUser(updatedStudent)
            return updatedStudent
        }
    }

    suspend fun changeConnectInfo(
        student: StudentEntity,
        address: String,
        phone: String,
        email: String,
        cvLink: String,
        linkedIn: String,
        facebook: String
    ): StudentEntity {
        val oldEmail = student.email?: ""
        helper.changeConnectInfo(
            student.id, address, phone, email, oldEmail, cvLink, linkedIn, facebook
        ).let { updatedStudent ->
            authRepo.storeSignInUser(updatedStudent)
            return updatedStudent
        }
    }
}