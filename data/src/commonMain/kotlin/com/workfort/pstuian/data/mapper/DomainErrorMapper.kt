package com.workfort.pstuian.data.mapper

import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode

class DomainErrorMapper {

    fun map(networkError: NetworkError): DomainError {
        val domainErrorCode: DomainErrorCode = when (val code = networkError.code) {
            is NetworkErrorCode.FirebaseAuth -> code.mapToAppErrorCode()
            is NetworkErrorCode.FireStore -> code.mapToAppErrorCode()
            is ApiResponseCode -> code.mapToAppErrorCode()
            else -> DomainErrorCode.Auth.InternalError // Handle UNKNOWN or other cases
        }

        return DomainError(domainErrorCode, networkError.exception)
    }

    private fun NetworkErrorCode.FirebaseAuth.mapToAppErrorCode(): DomainErrorCode.Auth {
        return when (this) {
            NetworkErrorCode.FirebaseAuth.InternalError -> DomainErrorCode.Auth.InternalError
            NetworkErrorCode.FirebaseAuth.UserNotFound -> DomainErrorCode.Auth.UserAuthNotFound
            NetworkErrorCode.FirebaseAuth.UserCreationFailed -> DomainErrorCode.Auth.UserAuthCreationFailed
            NetworkErrorCode.FirebaseAuth.UserNotVarified -> DomainErrorCode.Auth.UserNotVarified
            NetworkErrorCode.FirebaseAuth.UserAlreadyVarified -> DomainErrorCode.Auth.UserAlreadyVarified
        }
    }

    private fun NetworkErrorCode.FireStore.mapToAppErrorCode(): DomainErrorCode.Auth {
        return when (this) {
            NetworkErrorCode.FireStore.InternalError -> DomainErrorCode.Auth.InternalError
            NetworkErrorCode.FireStore.UserNotFound -> DomainErrorCode.Auth.UserProfileNotFound
            NetworkErrorCode.FireStore.UserCreationFailed -> DomainErrorCode.Auth.UserProfileCreationFailed
            NetworkErrorCode.FireStore.UserBlockListed -> DomainErrorCode.Auth.UserBlockListed
            NetworkErrorCode.FireStore.UserAlreadyExist -> DomainErrorCode.Auth.UserAlreadyExist
        }
    }

    private fun ApiResponseCode.mapToAppErrorCode(): DomainErrorCode {
        return when (this) {
            ApiResponseCode.Success -> {
                // success case shouldn't be coming in error mapper
                DomainErrorCode.Auth.InternalError
            }
            ApiResponseCode.MissingParam -> DomainErrorCode.Auth.MissingParam
            ApiResponseCode.InvalidParam -> DomainErrorCode.Auth.InvalidParam
            ApiResponseCode.ReadFailed -> DomainErrorCode.Auth.ReadFailed
            ApiResponseCode.WriteFailed -> DomainErrorCode.Auth.WriteFailed
            ApiResponseCode.AuthFailed -> DomainErrorCode.Auth.AuthFailed
            ApiResponseCode.ValidationFailed -> DomainErrorCode.Auth.ValidationFailed
            ApiResponseCode.Unknown -> DomainErrorCode.Auth.InternalError
            ApiResponseCode.InvalidAuthToken -> DomainErrorCode.Auth.InvalidAuthToken
            ApiResponseCode.DeviceNotFound -> DomainErrorCode.Auth.DeviceNotFound
            ApiResponseCode.DeviceRegistrationFailed -> DomainErrorCode.Auth.DeviceRegistrationFailed
            ApiResponseCode.DeviceBlockListed -> DomainErrorCode.Auth.DeviceBlockListed
            ApiResponseCode.DeviceAlreadyExist -> DomainErrorCode.Auth.DeviceAlreadyExist
            ApiResponseCode.UserNotFound -> DomainErrorCode.Auth.UserAuthNotFound
            ApiResponseCode.UserRegistrationFailed -> DomainErrorCode.Auth.UserProfileCreationFailed
            ApiResponseCode.UserBlockListed -> DomainErrorCode.Auth.UserBlockListed
            ApiResponseCode.UserAlreadyExist -> DomainErrorCode.Auth.UserAlreadyExist
        }
    }
}