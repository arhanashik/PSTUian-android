package com.workfort.pstuian.app.ui.common.facultypicker

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.FacultyEntity


data class FacultyPickerScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val panelState: PanelState,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                panelState = PanelState.None,
            )
        }

        sealed interface PanelState {
            data object None : PanelState
            data class SelectFaculty(
                val currentSelection: FacultyEntity?,
                val faculties: List<FacultyEntity>,
            ) : PanelState
            data class SelectBatch(
                val selectedFaculty: FacultyEntity,
                val currentSelection: BatchEntity?,
                val batches: List<BatchEntity>,
            ) : PanelState
            data class Error(val message: String) : PanelState
        }
    }

    sealed interface NavigationState {
        data class GoBack(
            val selectedFacultyId: Int?,
            val selectedBatchId: Int?,
        ) : NavigationState
    }
}