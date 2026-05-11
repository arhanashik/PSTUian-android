package com.workfort.pstuian.data.remote.firebase

import com.workfort.pstuian.data.model.AuthUserDto
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.util.PlatformInfo
import dev.gitlive.firebase.auth.ActionCodeSettings
import dev.gitlive.firebase.auth.AndroidPackageName
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseAuthDataSource(
    private val auth: FirebaseAuth,
    private val platformInfo: PlatformInfo,
) {

    suspend fun signIn(email: String, password: String): NetworkResult<AuthUserDto> {
        try {
            // firebase sign-in and validation
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).user
            return when {
                firebaseUser == null -> {
                    NetworkResult.failure(error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotFound))
                }

                firebaseUser.isEmailVerified.not() -> {
                    if (platformInfo.isDebug) {
                        NetworkResult.success(firebaseUser.toAuthUserDto())
                    } else {
                        auth.signOut()
                        NetworkResult.failure(
                            error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotVarified),
                        )
                    }
                }

                else -> NetworkResult.success(firebaseUser.toAuthUserDto())
            }
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
    ): NetworkResult<AuthUserDto> {
        try {
            // Create firebase user. Firebase throw errors if account already exists.
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).user
                ?: return NetworkResult.failure(
                    error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserRegistrationFailed),
                )
            firebaseUser.sendEmailVerification()

            return NetworkResult.success(firebaseUser.toAuthUserDto())
        } catch (exception: Throwable) {
            Napier.e("testR", exception)
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun signOut(): NetworkResult<Unit> {
        return try {
            auth.signOut()
            NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun sendVerificationEmail(email: String, password: String): NetworkResult<Unit> {
        try {
            // firebase sign-in and send email varification
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).user

            return when {
                firebaseUser == null -> {
                    NetworkResult.failure(
                        error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotFound),
                    )
                }

                firebaseUser.isEmailVerified -> {
                    auth.signOut()
                    NetworkResult.failure(
                        error = NetworkError(
                            code = NetworkErrorCode.FirebaseAuth.UserAlreadyVarified,
                        ),
                    )
                }

                else -> {
                    firebaseUser.sendEmailVerification()
                    auth.signOut()
                    NetworkResult.success(Unit)
                }
            }
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun resetPassword(email: String): NetworkResult<Unit> {
        return try {
            auth.sendPasswordResetEmail(email)
            NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun confirmPasswordReset(oobCode: String, newPassword: String): NetworkResult<Unit> {
        return try {
            auth.confirmPasswordReset(oobCode, newPassword)
            NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun sendPasswordResetEmail(email: String): NetworkResult<Unit> {
        val settings = ActionCodeSettings(
            url = "https://auth-dev.pstuian.com/auth?action=resetPassword",
            linkDomain = "auth-dev.pstuian.com",
            androidPackageName = AndroidPackageName(
                packageName = "com.workfort.pstuian.debug",
                installIfNotAvailable = true,
                minimumVersion = "",
            ),
            dynamicLinkDomain = "",
            canHandleCodeInApp = true,
            iOSBundleId = "com.workfort.pstuian.debug",
        )

        return try {
            auth.sendPasswordResetEmail(email = email, actionCodeSettings = settings)
            NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun updatePassword(password: String, newPassword: String): NetworkResult<Unit> {
        return try {
            val user = auth.currentUser ?: return NetworkResult.failure(
                error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotFound),
            )
            val email = user.email ?: return NetworkResult.failure(
                error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotFound),
            )
            // Re-authenticate to allow password change
            auth.signInWithEmailAndPassword(email, password)
            user.updatePassword(newPassword)
            NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    fun getCurrentUser(): AuthUserDto? {
        return auth.currentUser?.toAuthUserDto()
    }

    fun observeCurrentUser(): Flow<AuthUserDto?> {
        return auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toAuthUserDto()
        }
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun isUserEmailVerified(): Boolean = auth.currentUser?.isEmailVerified ?: false

    suspend fun getAuthToken(forceRefresh: Boolean = false): String? = auth.currentUser?.getIdToken(forceRefresh)

    suspend fun updateDisplayName(userId: String, displayName: String): NetworkResult<Unit> {
        return try {
            val user = auth.currentUser ?: return NetworkResult.failure(
                error = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotFound),
            )
            // Update profile with display name in Firebase Auth
            user.updateProfile(displayName = displayName)

            NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    suspend fun deleteAccount(email: String, password: String): NetworkResult<Unit> {
        val userNotFoundError = NetworkError(code = NetworkErrorCode.FirebaseAuth.UserNotFound)
        try {
            // existing account check
            val authUser = auth.signInWithEmailAndPassword(email, password).user
                ?: return NetworkResult.failure(error = userNotFoundError)

            // delete firebase auth user
            authUser.delete()
            return NetworkResult.success(Unit)
        } catch (exception: Throwable) {
            return NetworkResult.failure(error = CommonNetworkError.firebaseInternalError(exception))
        }
    }

    private fun FirebaseUser.toAuthUserDto(): AuthUserDto {
        return AuthUserDto(
            userId = uid,
            email = email.orEmpty(),
        )
    }
}
    