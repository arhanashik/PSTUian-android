package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.data.remote.service.TeacherApiService

class TeacherApiHelperImpl(private val service: TeacherApiService) : TeacherApiHelper {

    override suspend fun get(id: Int): NetworkResult<TeacherDto> {
        return runCatching {
            service.get(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getByEmail(email: String): NetworkResult<TeacherDto> {
        return runCatching {
            service.getByEmail(email).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeProfileImage(id: Int, imageUrl: String): Boolean {
        val response = service.changeProfileImage(id, imageUrl)
        if(!response.isSuccess) throw Exception(response.message)
        return response.isSuccess
    }

    override suspend fun changeName(id: Int, name: String): Boolean {
        val response = service.changeName(id, name)
        if(!response.isSuccess) throw Exception(response.message)
        return response.isSuccess
    }

    override suspend fun changeBio(id: Int, bio: String): Boolean {
        val response = service.changeBio(id, bio)
        if(!response.isSuccess) throw Exception(response.message)
        return response.isSuccess
    }

    override suspend fun changeAcademicInfo(
        id: Int,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): TeacherDto {
        val response = service.changeAcademicInfo(id, name, designation, department,
            blood, facultyId)
        if(!response.isSuccess) throw Exception(response.message)
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
    ): TeacherDto {
        val response = service.changeConnectInfo(id, address, phone,
            email, oldEmail, linkedIn, fbLink)
        if(!response.isSuccess) throw Exception(response.message)
        return response.data?: throw Exception("Empty data")
    }
}