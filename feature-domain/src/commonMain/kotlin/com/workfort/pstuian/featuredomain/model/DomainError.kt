package com.workfort.pstuian.featuredomain.model

data class DomainError(
    val code: DomainErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception)

sealed interface DomainErrorCode {

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
        UserAuthRegistrationFailed,
        UserRegistrationFailed,
        UserNotVarified,
        UserAlreadyVarified,
        UserBlockListed,
        UserAlreadyExist,
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
}