package com.workfort.pstuian.data.model

import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException

class NetworkError(
    val code: NetworkErrorCode,
    val exception: Throwable? = null,
) : Throwable(exception?.message, exception)

sealed interface NetworkErrorCode {

    enum class FirebaseAuth(val code: String): NetworkErrorCode {
        InternalError("FA000"),
        UserNotFound("FA001"),
        UserRegistrationFailed("FA002"),
        UserAlreadyRegistered("FA003"),
        UserNotVarified("FA004"),
        UserAlreadyVarified("FA005"),
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

    fun firebaseInternalError(exception: Throwable): NetworkError {
        val code = when (exception) {
            is FirebaseAuthInvalidCredentialsException -> NetworkErrorCode.FirebaseAuth.UserNotFound // Email already exists
            is FirebaseAuthUserCollisionException -> NetworkErrorCode.FirebaseAuth.UserAlreadyRegistered // Email already exists
            else -> NetworkErrorCode.FirebaseAuth.InternalError
        }
        return NetworkError(
            code = code,
            exception = exception,
        )
    }

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
