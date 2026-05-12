package com.workfort.pstuian.ui.faculty.batch.state

sealed interface BatchNavigationState {
    data class GoToStudents(val batchId: Int) : BatchNavigationState
}
