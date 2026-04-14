package com.workfort.pstuian.ui.facultypicker.state

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity

data class FacultyPickerUiState(
    val panelState: PanelState = PanelState.None,
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigationState: FacultyPickerNavigationState? = null,
) {
    sealed interface PanelState {
        data object None : PanelState
        data class SelectFaculty(
            val faculties: List<FacultyEntity>,
            val currentSelection: FacultyEntity?,
        ) : PanelState
        data class SelectBatch(
            val selectedFaculty: FacultyEntity,
            val batches: List<BatchEntity>,
            val currentSelection: BatchEntity?,
        ) : PanelState
        data class Error(val message: String) : PanelState
    }
}
