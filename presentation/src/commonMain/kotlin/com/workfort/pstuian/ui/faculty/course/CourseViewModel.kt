package com.workfort.pstuian.ui.faculty.course

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.course.state.CourseUiEvent
import com.workfort.pstuian.ui.faculty.course.state.CourseUiState

class CourseViewModel(
    private val facultyId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: CourseUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CourseUiState>(uiStateMachine) {

    private val courseListCache = mutableListOf<Course>()
    private var currentPage = 1
    private var hasMoreData = true

    override fun onUiReady() {
        getCourses(forceRefresh = true)
    }

    fun onUiEvent(event: CourseUiEvent) {
        when (event) {
            is CourseUiEvent.LoadMore -> getCourses(forceRefresh = false)
            is CourseUiEvent.CourseClicked -> Unit
        }
    }

    private fun getCourses(forceRefresh: Boolean) {
        if (forceRefresh) {
            courseListCache.clear()
            currentPage = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            facultyRepo.getCourses(facultyId, currentPage, forceRefresh).onSuccess { courses ->
                if (courses.isEmpty()) {
                    hasMoreData = false
                } else {
                    currentPage++
                }
                courseListCache.addAll(courses)
                uiStateMachine.showCourses(courseListCache.toList())
            }.onFailure {
                uiStateMachine.showContentLoading(isLoading = false)
                if (courseListCache.isEmpty()) {
                    val message = it.message ?: "Failed to load courses"
                    uiStateMachine.showError(message)
                }
            }
        }
    }
}
