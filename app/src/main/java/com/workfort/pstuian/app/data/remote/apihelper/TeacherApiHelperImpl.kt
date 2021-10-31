package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.util.remote.TeacherApiService

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
 *  * Last edited by : arhan on 11/01/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class TeacherApiHelperImpl(private val service: TeacherApiService) : TeacherApiHelper {
    override suspend fun changeProfileImage(id: Int, imageUrl: String): Boolean {
        val response = service.changeProfileImage(id, imageUrl)
        if(!response.success) throw Exception(response.message)
        return response.success
    }

    override suspend fun changeName(id: Int, name: String): Boolean {
        val response = service.changeName(id, name)
        if(!response.success) throw Exception(response.message)
        return response.success
    }

    override suspend fun changeBio(id: Int, bio: String): Boolean {
        val response = service.changeBio(id, bio)
        if(!response.success) throw Exception(response.message)
        return response.success
    }

    override suspend fun changeAcademicInfo(
        id: Int,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): TeacherEntity {
        val response = service.changeAcademicInfo(id, name, designation, department,
            blood, facultyId)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("Empty data")
    }

    override suspend fun changeConnectInfo(
        id: Int,
        address: String,
        phone: String,
        email: String,
        oldEmail: String,
        linkedIn: String,
        fbLink: String
    ): TeacherEntity {
        val response = service.changeConnectInfo(id, address, phone,
            email, oldEmail, linkedIn, fbLink)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("Empty data")
    }
}