package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Nov, 2021 at 1:15 AM.
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

interface TeacherApiHelper {
    suspend fun get(id: Int): TeacherEntity

    suspend fun changeProfileImage(
        id: Int,
        imageUrl: String,
    ): Boolean

    suspend fun changeName(id: Int, name: String): Boolean

    suspend fun changeBio(id: Int, bio: String): Boolean

    suspend fun changeAcademicInfo(
        id: Int,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): TeacherEntity

    suspend fun changeConnectInfo(
        id: Int,
        address: String,
        phone: String,
        email: String,
        oldEmail: String,
        linkedIn: String,
        fbLink: String
    ): TeacherEntity
}