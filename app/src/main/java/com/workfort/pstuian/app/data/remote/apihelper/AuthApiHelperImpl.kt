package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.util.remote.AuthApiService

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

class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {
    override suspend fun getConfig(): ConfigEntity {
        val response = service.getConfig()
        if(!response.success) throw Exception(response.message)
        return response.data!!
    }

    override suspend fun signIn(
        email: String,
        password: String,
        userType: String
    ): Pair<StudentEntity, String> {
        val response = service.signIn(email, password, userType)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signUp(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String
    ): Pair<StudentEntity, String> {
        val response = service.signUp(name, id, reg, facultyId, batchId, session)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signOut(id: Int, userType: String): String {
        val response = service.signOut(id, userType)
        if(!response.success) throw Exception(response.message)
        return response.message
    }

}