package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.dto.StudentDto

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 5:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface StudentApiHelper {
    suspend fun get(id: Int): StudentDto

    suspend fun changeProfileImage(
        id: Int,
        imageUrl: String,
    ): Boolean

    suspend fun changeName(id: Int, name: String): Boolean

    suspend fun changeBio(id: Int, bio: String): Boolean

    suspend fun changeAcademicInfo(
        name: String,
        oldId: Int,
        id: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): StudentDto

    suspend fun changeConnectInfo(
        id: Int,
        address: String,
        phone: String,
        email: String,
        oldEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String
    ): StudentDto
}