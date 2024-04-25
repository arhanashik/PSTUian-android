package com.workfort.pstuian.app.ui.common.facultypicker

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.view.service.StateUpdate

sealed interface FacultyPickerScreenStateUpdate : StateUpdate<FacultyPickerScreenState> {

    data class UpdateLoading(val isLoading: Boolean) : FacultyPickerScreenStateUpdate {
        override fun invoke(
            oldState: FacultyPickerScreenState,
        ): FacultyPickerScreenState  = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class ShowSelectFacultyPanel(
        val currentSelection: FacultyEntity?,
        val faculties: List<FacultyEntity>,
    ) : FacultyPickerScreenStateUpdate {
        override fun invoke(
            oldState: FacultyPickerScreenState
        ): FacultyPickerScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    isLoading = false,
                    panelState = FacultyPickerScreenState.DisplayState.PanelState.SelectFaculty(
                        currentSelection = currentSelection,
                        faculties = faculties,
                    ),
                ),
            )
        }
    }

    data class ShowSelectBatchPanel(
        val selectedFaculty: FacultyEntity,
        val currentSelection: BatchEntity?,
        val batches: List<BatchEntity>,
    ) : FacultyPickerScreenStateUpdate {
        override fun invoke(
            oldState: FacultyPickerScreenState
        ): FacultyPickerScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    isLoading = false,
                    panelState = FacultyPickerScreenState.DisplayState.PanelState.SelectBatch(
                        selectedFaculty = selectedFaculty,
                        currentSelection= currentSelection,
                        batches = batches,
                    ),
                ),
            )
        }
    }

    data class DataLoadFailed(val message: String) : FacultyPickerScreenStateUpdate {
        override fun invoke(
            oldState: FacultyPickerScreenState
        ): FacultyPickerScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    isLoading = false,
                    panelState = FacultyPickerScreenState.DisplayState.PanelState.Error(message),
                ),
            )
        }
    }

    data class NavigateTo(
        val newState: FacultyPickerScreenState.NavigationState,
    ) : FacultyPickerScreenStateUpdate {
        override fun invoke(
            oldState: FacultyPickerScreenState,
        ): FacultyPickerScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : FacultyPickerScreenStateUpdate {
        override fun invoke(
            oldState: FacultyPickerScreenState,
        ): FacultyPickerScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}