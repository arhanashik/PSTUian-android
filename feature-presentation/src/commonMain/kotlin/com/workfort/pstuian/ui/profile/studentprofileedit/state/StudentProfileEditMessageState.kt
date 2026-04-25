package com.workfort.pstuian.ui.profile.studentprofileedit.state

sealed interface StudentProfileEditMessageState {
    data class Loading(val cancelable: Boolean) : StudentProfileEditMessageState
    data class ConfirmSave(val onConfirm: () -> Unit) : StudentProfileEditMessageState
    data class Success(val message: String) : StudentProfileEditMessageState
    data class Error(val message: String) : StudentProfileEditMessageState
}
