package com.workfort.pstuian.ui.contactus.state

sealed interface ContactUsMessageState {
    data class Error(val message: String) : ContactUsMessageState
    data class Success(val message: String) : ContactUsMessageState
}
