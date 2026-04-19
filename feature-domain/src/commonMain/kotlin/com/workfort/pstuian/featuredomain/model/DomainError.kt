package com.workfort.pstuian.featuredomain.model

data class DomainError(
    val code: DomainErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception)

sealed interface DomainErrorCode {

    enum class Auth(val code: String): DomainErrorCode {
        InternalError("SBA000"),
        UserAuthNotFound("SBA001"),
        UserProfileNotFound("SBA002"),
        UserAuthCreationFailed("SBA003"),
        UserProfileCreationFailed("SBa004"),
        UserNotVarified("SBA005"),
        UserAlreadyVarified("SBA006"),
        UserBlockListed("SBA007"),
        UserAlreadyExist("SBA008"),
        InvalidAuthToken("SBA009"),
        DeviceNotFound("SBA010"),
        DeviceRegistrationFailed("SBA011"),
        DeviceBlockListed("SBA012"),
        DeviceAlreadyExist("SBA013"),
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