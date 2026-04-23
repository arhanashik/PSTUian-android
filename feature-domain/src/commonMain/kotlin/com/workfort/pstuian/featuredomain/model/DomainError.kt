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
        UserProfileNotFound,
        UserAuthCreationFailed,
        UserProfileCreationFailed,
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

    enum class Validation(val code: String): DomainErrorCode {
        InputEmpty("SBV001"),
        InputTooLong("SBV002"),
        InputInvalid("SBV003"),

        UserInvalid("SBV004"),
        UserAlreadyAdded("SBV005"),
        ;
    }
}