package com.workfort.pstuian.ui.teacherprofileedit.state

sealed interface TeacherProfileEditMessageState {
    data class Loading(val cancelable: Boolean) : TeacherProfileEditMessageState
    data class ConfirmSave(val onConfirm: () -> Unit) : TeacherProfileEditMessageState
    data class Success(val message: String) : TeacherProfileEditMessageState
    data class Error(val message: String) : TeacherProfileEditMessageState
}
