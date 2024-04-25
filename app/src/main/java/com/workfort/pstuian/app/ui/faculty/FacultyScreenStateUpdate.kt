package com.workfort.pstuian.app.ui.faculty

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.view.service.StateUpdate

sealed interface FacultyScreenStateUpdate : StateUpdate<FacultyScreenState> {

    data class UpdateTitle(val title: String) : FacultyScreenStateUpdate {
        override fun invoke(oldState: FacultyScreenState): FacultyScreenState = with(oldState) {
            copy(displayState = displayState.copy(title = title))
        }
    }

    data class UpdateBatchListData(
        val batches: List<BatchEntity>,
        val isLoading: Boolean,
    ) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    batchListState = FacultyScreenState.DisplayState.BatchListState
                        .Available(batches, isLoading)
                )
            )
        }
    }

    data class LoadBatchListFailed(val message: String) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    batchListState = FacultyScreenState.DisplayState.BatchListState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateTeacherListData(
        val teachers: List<TeacherEntity>,
        val isLoading: Boolean,
    ) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    teacherListState = FacultyScreenState.DisplayState.TeacherListState
                        .Available(teachers, isLoading)
                )
            )
        }
    }

    data class LoadTeacherListFailed(val message: String) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    teacherListState = FacultyScreenState.DisplayState.TeacherListState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateCourseListData(
        val courses: List<CourseEntity>,
        val isLoading: Boolean,
    ) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    courseListState = FacultyScreenState.DisplayState.CourseListState
                        .Available(courses, isLoading)
                )
            )
        }
    }

    data class LoadCourseListFailed(val message: String) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    courseListState = FacultyScreenState.DisplayState.CourseListState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateEmployeeListData(
        val employees: List<EmployeeEntity>,
        val isLoading: Boolean,
    ) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    employeeListState = FacultyScreenState.DisplayState.EmployeeListState
                        .Available(employees, isLoading)
                )
            )
        }
    }

    data class LoadEmployeeListFailed(val message: String) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    employeeListState = FacultyScreenState.DisplayState.EmployeeListState
                        .Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val messageState: FacultyScreenState.DisplayState.MessageState
    ) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState
        ): FacultyScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: FacultyScreenState.NavigationState,
    ) : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState,
        ): FacultyScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : FacultyScreenStateUpdate {
        override fun invoke(
            oldState: FacultyScreenState,
        ): FacultyScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}