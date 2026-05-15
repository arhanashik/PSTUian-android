package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.StudentApiService

class StudentApiHelperImpl(private val service: StudentApiService) : StudentApiHelper {

    override suspend fun get(studentId: Int): NetworkResult<StudentDto> {
        return safeApiCall { service.get(studentId) }
    }

    override suspend fun getByEmail(email: String): NetworkResult<StudentDto> {
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

    override suspend fun changeCvUrl(fileUrl: String): NetworkResult<Unit> {
        return safeApiCall { service.changeCvUrl(fileUrl) }
    }

    override suspend fun changeAcademicInfo(
        name: String,
        studentOldId: Int,
        studentId: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): NetworkResult<StudentDto> {
        return safeApiCall {
            service.changeAcademicInfo(
                name = name,
                studentOldId = studentOldId,
                studentId = studentId,
                reg = reg,
                blood = blood,
                facultyId = facultyId,
                session = session,
                batchId = batchId,
            )
        }
    }

    override suspend fun changeConnectInfo(
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String,
    ): NetworkResult<StudentDto> {
        return safeApiCall {
            service.changeConnectInfo(
                address = address,
                phone = phone,
                oldEmail = oldEmail,
                newEmail = newEmail,
                cvLink = cvLink,
                linkedIn = linkedIn,
                fbLink = fbLink,
            )
        }
    }
}
