package com.workfort.pstuian.ui.facultypicker.state

import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity

sealed interface FacultyPickerUiEvent {
    data object OnLoadData : FacultyPickerUiEvent
    data object OnClickBack : FacultyPickerUiEvent
    data class OnClickFaculty(val faculty: FacultyEntity) : FacultyPickerUiEvent
    data class OnClickBatch(val batch: BatchEntity) : FacultyPickerUiEvent
    data object OnClickChangeFaculty : FacultyPickerUiEvent
    data object NavigationConsumed : FacultyPickerUiEvent
}
