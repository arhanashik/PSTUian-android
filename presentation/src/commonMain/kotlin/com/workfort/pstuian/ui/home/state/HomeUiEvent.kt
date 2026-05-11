package com.workfort.pstuian.ui.home.state

import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.Slider
import com.workfort.pstuian.ui.home.ActionItem

sealed interface HomeUiEvent {
    data object SignInClicked : HomeUiEvent
    data object UserProfileClicked : HomeUiEvent
    data object NotificationClicked : HomeUiEvent
    data class ScrollSlider(val position: Int) : HomeUiEvent
    data class SliderClicked(val slider: Slider) : HomeUiEvent
    data class FacultyClicked(val faculty: Faculty) : HomeUiEvent
    data class ActionItemClicked(val actionItem: ActionItem) : HomeUiEvent
    data object RequestNotificationPermissionClicked : HomeUiEvent
}