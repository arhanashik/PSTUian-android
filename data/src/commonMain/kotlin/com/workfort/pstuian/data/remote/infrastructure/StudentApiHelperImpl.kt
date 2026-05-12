package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.data.remote.service.StudentApiService

class StudentApiHelperImpl(private val service: StudentApiService) : StudentApiHelper {

    override suspend fun get(studentId: Int): NetworkResult<StudentDto> {
        return runCatching {
            service.get(studentId).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun getByEmail(email: String): NetworkResult<StudentDto> {
        return runCatching {
            service.getByEmail(email).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeProfileImage(imageUrl: String): NetworkResult<Unit> {
        return runCatching {
            service.changeProfileImage(imageUrl).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeName(name: String): NetworkResult<Unit> {
        return service.changeName(name).toNetworkResult()
    }

    override suspend fun changeBio(bio: String): NetworkResult<Unit> {
        return service.changeBio(bio).toNetworkResult()
    }

    override suspend fun changeCvUrl(fileUrl: String): NetworkResult<Unit> {
        return runCatching {
            service.changeCvUrl(fileUrl).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeAcademicInfo(
        name: String,
        studentOldId: Int,
        studentId: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): NetworkResult<StudentDto> {
        return runCatching {
            service.changeAcademicInfo(
                name = name,
                studentOldId = studentOldId,
                studentId = studentId,
                reg = reg,
                blood = blood,
                facultyId = facultyId,
                session = session,
                batchId = batchId,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeConnectInfo(
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String
    ): NetworkResult<StudentDto> {
        return runCatching {
            service.changeConnectInfo(
                address = address,
                phone = phone,
                oldEmail = oldEmail,
                newEmail = newEmail,
                cvLink = cvLink,
                linkedIn = linkedIn,
                fbLink = fbLink,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}