package com.workfort.pstuian.app.ui.home

import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.SliderEntity

sealed class HomeUiEvent {
    data object None : HomeUiEvent()
    data object LoadInitialData : HomeUiEvent()
    data object GetSliders : HomeUiEvent()
    data object GetFaculties : HomeUiEvent()
    data object GetUserProfile : HomeUiEvent()
    data object OnClickSignIn : HomeUiEvent()
    data object OnClickUserProfile : HomeUiEvent()
    data object OnClickNotification : HomeUiEvent()
    data class OnScrollSlider(val position: Int) : HomeUiEvent()
    data class OnClickSlider(val slider: SliderEntity) : HomeUiEvent()
    data class OnClickFaculty(val faculty: FacultyEntity) : HomeUiEvent()
    data class OnClickActionItem(val actionItem: ActionItem) : HomeUiEvent()
    data object OnSignIn : HomeUiEvent()
    data object OnRequestNotificationPermission : HomeUiEvent()
    data object OnClearData : HomeUiEvent()
    data object MessageConsumed : HomeUiEvent()
    data object NavigationConsumed : HomeUiEvent()
}