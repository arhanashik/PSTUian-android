package com.workfort.pstuian.ui.faculty.course.state

import com.workfort.pstuian.featuredomain.model.Course

sealed interface CourseUiEvent {
    data object LoadMore : CourseUiEvent
    data class CourseClicked(val course: Course) : CourseUiEvent
}
