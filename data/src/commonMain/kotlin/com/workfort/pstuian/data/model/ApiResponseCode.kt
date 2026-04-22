package com.workfort.pstuian.data.model

enum class ApiResponseCode(val code: String): NetworkErrorCode {
    Success("S00000"),
    MissingParam("S00001"),
    ReadFailed("S00002"),
    WriteFailed("S00003"),
    Unknown("S11111"),

    // Auth
    InvalidAuthToken("SA0001"),

    // Device
    DeviceNotFound("SD01"),
    DeviceRegistrationFailed("SD002"),
    DeviceBlockListed("SD003"),
    DeviceAlreadyExist("SD004"),

    // User
    UserNotFound("SU01"),
    UserCreationFailed("SU002"),
    UserBlockListed("SU003"),
    UserAlreadyExist("SU004"),
    ;

    fun isSuccess() = this == Success
    fun isError() = !isSuccess()

    companion object {
        fun create(code: String) = entries.firstOrNull { it.code == code } ?: Unknown
    }
}
