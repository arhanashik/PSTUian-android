package com.workfort.pstuian.featuredomain.model

data class DomainError(
    val code: DomainErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception)

sealed interface DomainErrorCode {

    enum class Auth(val code: String): DomainErrorCode {
        InternalError("SBA000"),
        MissingParam("SBA001"),
        ReadFailed("SBA002"),
        WriteFailed("SBA003"),
        UserAuthNotFound("SBA004"),
        UserProfileNotFound("SBA005"),
        UserAuthCreationFailed("SBA006"),
        UserProfileCreationFailed("SB007"),
        UserNotVarified("SBA008"),
        UserAlreadyVarified("SBA009"),
        UserBlockListed("SBA010"),
        UserAlreadyExist("SBA011"),
        InvalidAuthToken("SBA012"),
        DeviceNotFound("SBA013"),
        DeviceRegistrationFailed("SBA014"),
        DeviceBlockListed("SBA015"),
        DeviceAlreadyExist("SBA016"),
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