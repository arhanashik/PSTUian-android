package com.workfort.pstuian.ui.facultypicker.state

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Faculty

sealed interface FacultyPickerUiEvent {
    data object OnLoadData : FacultyPickerUiEvent
    data object OnClickBack : FacultyPickerUiEvent
    data class OnClickFaculty(val faculty: Faculty) : FacultyPickerUiEvent
    data class OnClickBatch(val batch: Batch) : FacultyPickerUiEvent
    data object OnClickChangeFaculty : FacultyPickerUiEvent
    data object NavigationConsumed : FacultyPickerUiEvent
}
