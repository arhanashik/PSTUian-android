package com.workfort.pstuian.ui.profile.studentprofileedit.state

sealed interface StudentProfileEditMessageState {
    data class Loading(val cancelable: Boolean = false) : StudentProfileEditMessageState
    data class ConfirmSave(val onConfirm: () -> Unit) : StudentProfileEditMessageState
    data class ShowSnackBar(val message: String) : StudentProfileEditMessageState
    data class Error(val message: String) : StudentProfileEditMessageState
}
