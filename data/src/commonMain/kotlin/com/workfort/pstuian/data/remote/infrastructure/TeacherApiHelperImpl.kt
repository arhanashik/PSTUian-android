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

    override suspend fun changeProfileImage(imageUrl: String): NetworkResult<Unit> {
        return runCatching {
            service.changeProfileImage(imageUrl).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeName(name: String): NetworkResult<Unit> {
        return runCatching {
            service.changeName(name).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeBio(bio: String): NetworkResult<Unit> {
        return runCatching {
            service.changeBio(bio).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeAcademicInfo(
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): NetworkResult<TeacherDto> {
        return runCatching {
            service.changeAcademicInfo(
                name,
                designation,
                department,
                blood,
                facultyId,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changeConnectInfo(
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): NetworkResult<TeacherDto> {
        return runCatching {
            service.changeConnectInfo(
                address,
                phone,
                oldEmail,
                email,
                linkedIn,
                fbLink,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}