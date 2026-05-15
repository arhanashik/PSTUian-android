package com.workfort.pstuian.data.mapper

import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode

object DomainErrorMapper {

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
            NetworkErrorCode.FirebaseAuth.UserRegistrationFailed -> DomainErrorCode.Auth.UserAuthRegistrationFailed
            NetworkErrorCode.FirebaseAuth.UserAlreadyRegistered -> DomainErrorCode.Auth.UserAuthAlreadyRegistered
            NetworkErrorCode.FirebaseAuth.UserNotVarified -> DomainErrorCode.Auth.UserNotVarified
            NetworkErrorCode.FirebaseAuth.UserAlreadyVarified -> DomainErrorCode.Auth.UserAlreadyVarified
        }
    }

    private fun NetworkErrorCode.FireStore.mapToAppErrorCode(): DomainErrorCode.Auth {
        return when (this) {
            NetworkErrorCode.FireStore.InternalError -> DomainErrorCode.Auth.InternalError
        }
    }

    private fun ApiResponseCode.mapToAppErrorCode(): DomainErrorCode {
        return when (this) {
            ApiResponseCode.Success -> DomainErrorCode.None // success case shouldn't be coming in error mapper
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
            ApiResponseCode.UserAuthUnregistered -> DomainErrorCode.Auth.UserAuthUnregistered
            ApiResponseCode.UserNotFound -> DomainErrorCode.Auth.UserNotFound
            ApiResponseCode.UserRegistrationFailed -> DomainErrorCode.Auth.UserRegistrationFailed
            ApiResponseCode.UserBlockListed -> DomainErrorCode.Auth.UserBlockListed
            ApiResponseCode.UserAlreadyExist -> DomainErrorCode.Auth.UserAlreadyExist
            ApiResponseCode.UserDeactivated -> DomainErrorCode.Auth.UserDeactivated
        }
    }
}