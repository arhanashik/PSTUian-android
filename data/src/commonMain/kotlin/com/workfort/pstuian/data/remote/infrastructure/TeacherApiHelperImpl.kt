package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.TeacherApiService

class TeacherApiHelperImpl(private val service: TeacherApiService) : TeacherApiHelper {

    override suspend fun get(id: Int): NetworkResult<TeacherDto> {
        return safeApiCall { service.get(id) }
    }

    override suspend fun getByEmail(email: String): NetworkResult<TeacherDto> {
        return safeApiCall { service.getByEmail(email) }
    }

    override suspend fun changeProfileImage(imageUrl: String): NetworkResult<Unit> {
        return safeApiCall { service.changeProfileImage(imageUrl) }
    }

    override suspend fun changeName(name: String): NetworkResult<Unit> {
        return safeApiCall { service.changeName(name) }
    }

    override suspend fun changeBio(bio: String): NetworkResult<Unit> {
        return safeApiCall { service.changeBio(bio) }
    }

    override suspend fun changeAcademicInfo(
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int,
    ): NetworkResult<TeacherDto> {
        return safeApiCall {
            service.changeAcademicInfo(name, designation, department, blood, facultyId)
        }
    }

    override suspend fun changeConnectInfo(
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String,
    ): NetworkResult<TeacherDto> {
        return safeApiCall {
            service.changeConnectInfo(address, phone, oldEmail, email, linkedIn, fbLink)
        }
    }
}
