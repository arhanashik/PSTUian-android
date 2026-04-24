package com.workfort.pstuian.ui.facultypicker.state

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty

data class FacultyPickerUiState(
    val panelState: PanelState = PanelState.None,
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigationState: FacultyPickerNavigationState? = null,
) {
    sealed interface PanelState {
        data object None : PanelState
        data class SelectFaculty(
            val faculties: List<Faculty>,
            val currentSelection: Faculty?,
        ) : PanelState
        data class SelectBatch(
            val selectedFaculty: Faculty,
            val batches: List<Batch>,
            val currentSelection: Batch?,
        ) : PanelState
        data class Error(val message: String) : PanelState
    }
}
