package com.workfort.pstuian.ui.faculty

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.state.FacultyMessageState
import com.workfort.pstuian.ui.faculty.state.FacultyNavigationState
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FacultyViewModel(
    private val facultyId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: FacultyUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<FacultyUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<FacultyMessageState?>(null)
    val message: StateFlow<FacultyMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<FacultyNavigationState?>(null)
    val navigation: StateFlow<FacultyNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        setInitialContent()
    }

    fun onUiEvent(event: FacultyUiEvent) {
        when (event) {
            is FacultyUiEvent.BackClicked -> _navigation.update { FacultyNavigationState.GoBack }
            is FacultyUiEvent.SelectTab -> uiStateMachine.selectTab(event.index)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationConsumed() = _navigation.update { null }

    private fun setInitialContent() {
        uiStateMachine.showLoadingOverlay(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            facultyRepo.getFaculty(facultyId).onSuccess { faculty ->
                uiStateMachine.setInitialContent(
                    facultyId = facultyId,
                    title = faculty.title,
                    tabs = listOf("Batch", "Teacher", "Course", "Employee"),
                    selectedTab = 0,
                )
            }.onFailure { error ->
                _message.update {
                    FacultyMessageState.ShowError(message = error.message ?: "Failed to load data") {
                        onMessageHandled()
                        setInitialContent()
                    }
                }
            }
        }
    }
}
