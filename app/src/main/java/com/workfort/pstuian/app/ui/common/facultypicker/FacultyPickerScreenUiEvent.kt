package com.workfort.pstuian.app.ui.common.facultypicker

import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.FacultyEntity

sealed class FacultyPickerScreenUiEvent {
    data object None : FacultyPickerScreenUiEvent()
    data object OnLoadData : FacultyPickerScreenUiEvent()
    data object OnClickBack : FacultyPickerScreenUiEvent()
    data class OnClickFaculty(val faculty: FacultyEntity) : FacultyPickerScreenUiEvent()
    data class OnClickBatch(val batch: BatchEntity) : FacultyPickerScreenUiEvent()
    data object OnClickChangeFaculty : FacultyPickerScreenUiEvent()
    data object NavigationConsumed : FacultyPickerScreenUiEvent()
}