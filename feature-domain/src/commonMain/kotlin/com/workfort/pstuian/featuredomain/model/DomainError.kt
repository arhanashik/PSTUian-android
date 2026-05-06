package com.workfort.pstuian.featuredomain.model

data class DomainError(
    val code: DomainErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception)

sealed interface DomainErrorCode {

    data object None : DomainErrorCode

    enum class Auth: DomainErrorCode {
        InternalError,
        MissingParam,
        InvalidParam,
        ReadFailed,
        WriteFailed,
        AuthFailed,
        ValidationFailed,
        UserAuthNotFound,
        UserNotFound,
        UserAuthUnregistered, // legacy user without valid auth-user-id
        UserAuthRegistrationFailed,
        UserAuthAlreadyRegistered,
        UserRegistrationFailed,
        UserNotVarified,
        UserAlreadyVarified,
        UserBlockListed,
        UserAlreadyExist,
        UserDeactivated,
        InvalidAuthToken,
        DeviceNotFound,
        DeviceRegistrationFailed,
        DeviceBlockListed,
        DeviceAlreadyExist,
        ;
    }

    enum class Validation: DomainErrorCode {
        InputEmpty,
        InputTooLong,
        InputInvalid,
        ;
    }

    enum class File : DomainErrorCode {
        DownloadFailed,
        SaveFailed,
        ;
    }
}