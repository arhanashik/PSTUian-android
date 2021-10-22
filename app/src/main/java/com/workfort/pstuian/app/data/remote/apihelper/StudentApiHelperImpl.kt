package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.util.remote.StudentApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 3:52 PM.
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

class StudentApiHelperImpl(private val service: StudentApiService) : StudentApiHelper {
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

    override suspend fun changeAcademicInfo(
        oldId: Int,
        id: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): StudentEntity {
        val response = service.changeAcademicInfo(oldId, id, reg, blood,
            facultyId, session, batchId)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("Empty data")
    }

    override suspend fun changeConnectInfo(
        id: Int,
        address: String,
        phone: String,
        email: String,
        oldEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String
    ): StudentEntity {
        val response = service.changeConnectInfo(id, address, phone,
            email, oldEmail, cvLink, linkedIn, fbLink)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("Empty data")
    }
}