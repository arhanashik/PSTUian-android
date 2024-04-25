package com.workfort.pstuian.app.ui.faculty

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.TeacherEntity

sealed class FacultyScreenUiEvent {
    data object None : FacultyScreenUiEvent()
    data object LoadInitialData : FacultyScreenUiEvent()
    data object OnClickBack : FacultyScreenUiEvent()
    data class OnClickBatch(val batch: BatchEntity) : FacultyScreenUiEvent()
    data class OnClickTeacher(val teacher: TeacherEntity) : FacultyScreenUiEvent()
    data class OnClickCourse(val course: CourseEntity) : FacultyScreenUiEvent()
    data class OnClickEmployee(val employee: EmployeeEntity) : FacultyScreenUiEvent()
    data class OnClickCall(val phoneNumber: String) : FacultyScreenUiEvent()
    data class OnCall(val phoneNumber: String) : FacultyScreenUiEvent()
    data object MessageConsumed : FacultyScreenUiEvent()
    data object NavigationConsumed : FacultyScreenUiEvent()
}