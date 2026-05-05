package com.workfort.pstuian.featuredomain.model

data class DomainError(
    val code: DomainErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception) {

    // users without valid user id
    val isLegacyUserAccountError
        get() = code == DomainErrorCode.Auth.UserIdInvalid
}

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
        UserAuthAlreadyRegistered,
        UserRegistrationFailed,
        UserNotVarified,
        UserAlreadyVarified,
        UserBlockListed,
        UserAlreadyExist,
        UserIdInvalid, // legacy user without valid user id
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