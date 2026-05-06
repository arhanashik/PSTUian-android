package com.workfort.pstuian.data.model

enum class ApiResponseCode(val code: String): NetworkErrorCode {
    Success("S00000"),
    MissingParam("S00001"),
    InvalidParam("S00002"),
    ReadFailed("S00003"),
    WriteFailed("S00004"),
    AuthFailed("S00005"),
    ValidationFailed("S00006"),
    Unknown("S11111"),

    // Auth
    InvalidAuthToken("SA0001"),

    // Device
    DeviceNotFound("SD01"),
    DeviceRegistrationFailed("SD002"),
    DeviceBlockListed("SD003"),
    DeviceAlreadyExist("SD004"),

    // User
    UserAuthUnregistered("SU000"), // legacy user without valid user id
    UserNotFound("SU001"),
    UserRegistrationFailed("SU002"),
    UserBlockListed("SU003"),
    UserAlreadyExist("SU004"),
    UserDeactivated("SU005"),
    ;

    fun isSuccess() = this == Success
    fun isError() = !isSuccess()

    companion object {
        fun create(code: String) = entries.firstOrNull { it.code == code } ?: Unknown
    }
}
