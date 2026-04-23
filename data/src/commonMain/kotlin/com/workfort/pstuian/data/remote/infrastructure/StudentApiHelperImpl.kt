package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.data.remote.service.StudentApiService

class StudentApiHelperImpl(private val service: StudentApiService) : StudentApiHelper {

    override suspend fun get(userId: String): StudentDto? {
        return service.get(userId).data
    }

    override suspend fun getByEmail(email: String): NetworkResult<StudentDto> {
        return runCatching {
            service.getByEmail(email).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeProfileImage(userId: String, imageUrl: String): NetworkResult<Unit> {
        return service.changeProfileImage(userId, imageUrl).toNetworkResult()
    }

    override suspend fun changeName(userId: String, name: String): NetworkResult<Unit> {
        return service.changeName(userId, name).toNetworkResult()
    }

    override suspend fun changeBio(userId: String, bio: String): NetworkResult<Unit> {
        return service.changeBio(userId, bio).toNetworkResult()
    }

    override suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        studentId: String,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): NetworkResult<Unit> {
        return service.changeAcademicInfo(
            userId = userId,
            name = name,
            studentId = studentId,
            reg = reg,
            blood = blood,
            facultyId = facultyId,
            session = session,
            batchId = batchId,
        ).toNetworkResult()
    }

    override suspend fun changeConnectInfo(
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String
    ): NetworkResult<Unit> {
        return service.changeConnectInfo(
            userId = userId,
            address = address,
            phone = phone,
            oldEmail = oldEmail,
            newEmail = newEmail,
            cvLink = cvLink,
            linkedIn = linkedIn,
            fbLink = fbLink,
        ).toNetworkResult()
    }
}