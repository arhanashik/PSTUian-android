package com.workfort.pstuian.ui.students.state

sealed interface MessageState {
    data class Call(val phoneNumber: String) : MessageState
}
