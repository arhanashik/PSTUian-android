package com.workfort.pstuian.ui.signin.screendata

import com.workfort.pstuian.featuredomain.model.DomainErrorCode

internal fun DomainErrorCode.mapToErrorMessageForSignInScreen(): String? = when (this) {
    is DomainErrorCode.Auth -> mapToMessage()
    is DomainErrorCode.Validation -> mapToMessage()
}

private fun DomainErrorCode.Auth.mapToMessage(): String? = when (this) {
    DomainErrorCode.Auth.InternalError ->
        "Something went wrong. Please try again."
    DomainErrorCode.Auth.MissingParam ->
        "Required information is missing. Please check your input and try again."
    DomainErrorCode.Auth.InvalidParam ->
        "Some information is not valid. Please check your input and try again."
    DomainErrorCode.Auth.ReadFailed ->
        "Could not load your information. Please try again."
    DomainErrorCode.Auth.WriteFailed ->
        "Could not save your information. Please try again."
    DomainErrorCode.Auth.AuthFailed ->
        "Authentication failed. Please check your email and password and try again."
    DomainErrorCode.Auth.ValidationFailed ->
        "Credentials could not be validated. Please enter valid credentials and try again."
    DomainErrorCode.Auth.UserAuthNotFound ->
        "No auth account found for this email. Please sign up or use a different email."
    DomainErrorCode.Auth.UserAuthRegistrationFailed ->
        "Could not register auth account. Please try again."
    DomainErrorCode.Auth.UserAuthAlreadyRegistered ->
        "An auth account with this email already exists. Try signing in instead. Or contact support."
    DomainErrorCode.Auth.UserIdInvalid ->
        "Legacy User Account. Please contact support."
    DomainErrorCode.Auth.UserNotFound ->
        "Your profile could not be found. Please check email and password and retry."
    DomainErrorCode.Auth.UserRegistrationFailed ->
        "Could not complete registration. Please try again."
    DomainErrorCode.Auth.UserNotVarified ->
        "Please verify your email address before signing in. Check your inbox for a verification link."
    DomainErrorCode.Auth.UserAlreadyVarified ->
        "This email is already verified. You can sign in."
    DomainErrorCode.Auth.UserBlockListed ->
        "Your account is not allowed to access the app. Please contact support."
    DomainErrorCode.Auth.UserAlreadyExist ->
        "An account with this email already exists. Try signing in instead."
    DomainErrorCode.Auth.DeviceNotFound ->
        "This device is not registered. Please try again or contact support."
    DomainErrorCode.Auth.DeviceRegistrationFailed ->
        "Could not register this device. Please try again."
    DomainErrorCode.Auth.DeviceBlockListed ->
        "This device is not allowed to access the app. Please contact support."
    else -> null
}

private fun DomainErrorCode.Validation.mapToMessage(): String = when (this) {
    DomainErrorCode.Validation.InputEmpty -> "Input cannot be empty"
    DomainErrorCode.Validation.InputTooLong -> "Input length is too long"
    DomainErrorCode.Validation.InputInvalid -> "Invalid input"
}