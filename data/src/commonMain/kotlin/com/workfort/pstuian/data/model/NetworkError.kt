package com.workfort.pstuian.data.model

class NetworkError(
    val code: NetworkErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception)

sealed interface NetworkErrorCode {

    enum class FirebaseAuth(val code: String): NetworkErrorCode {
        InternalError("FA000"),
        UserNotFound("FA001"),
        UserRegistrationFailed("FA002"),
        UserNotVarified("FA003"),
        UserAlreadyVarified("FA004"),
        ;
    }

    enum class FireStore(val code: String): NetworkErrorCode {
        InternalError("FS000"),
        ;
    }

    enum class Server(val code: String): NetworkErrorCode {
        InternalError("SE000"),
        ;
    }

    object UNKNOWN : NetworkErrorCode
}

object CommonNetworkError {

    fun firebaseInternalError(exception: Throwable) = NetworkError(
        code = NetworkErrorCode.FirebaseAuth.InternalError,
        exception = exception,
    )

    fun fireStoreInternalError(exception: Throwable) = NetworkError(
        code = NetworkErrorCode.FireStore.InternalError,
        exception = exception,
    )

    fun apiError(code: ApiResponseCode, exception: Throwable? = null): NetworkError {
        return NetworkError(code, exception)
    }

    val UNAUTHORIZED = NetworkError(NetworkErrorCode.FireStore.InternalError) // Placeholder
    val UNKNOWN = NetworkError(NetworkErrorCode.UNKNOWN)
}
