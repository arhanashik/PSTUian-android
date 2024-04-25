package com.workfort.pstuian.app.ui.faculty

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.TeacherEntity


data class FacultyScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val title: String,
        val batchListState: BatchListState,
        val teacherListState: TeacherListState,
        val courseListState: CourseListState,
        val employeeListState: EmployeeListState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                title = "",
                batchListState = BatchListState.None,
                teacherListState = TeacherListState.None,
                courseListState = CourseListState.None,
                employeeListState = EmployeeListState.None,
                messageState = null,
            )
        }

        sealed interface BatchListState {
            data object None : BatchListState
            data class Available(
                val batches: List<BatchEntity>,
                val isLoading: Boolean,
            ) : BatchListState
            data class Error(val message: String) : BatchListState
        }

        sealed interface TeacherListState {
            data object None : TeacherListState
            data class Available(
                val teachers: List<TeacherEntity>,
                val isLoading: Boolean,
            ) : TeacherListState
            data class Error(val message: String) : TeacherListState
        }

        sealed interface CourseListState {
            data object None : CourseListState
            data class Available(
                val courses: List<CourseEntity>,
                val isLoading: Boolean,
            ) : CourseListState
            data class Error(val message: String) : CourseListState
        }

        sealed interface EmployeeListState {
            data object None : EmployeeListState
            data class Available(
                val employees: List<EmployeeEntity>,
                val isLoading: Boolean,
            ) : EmployeeListState
            data class Error(val message: String) : EmployeeListState
        }
        sealed interface MessageState {
            data class Call(val phoneNumber: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data class GoToStudentsScreen(val batch: BatchEntity) : NavigationState
        data class GoToTeacherScreen(val teacher: TeacherEntity) : NavigationState
        data class GoToEmployeeScreen(val employee: EmployeeEntity) : NavigationState
    }
}