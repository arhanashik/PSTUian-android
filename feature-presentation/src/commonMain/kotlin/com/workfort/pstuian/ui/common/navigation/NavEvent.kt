package com.workfort.pstuian.ui.common.navigation

sealed class NavEvent {
    data class Navigate(val screen: AppScreen) : NavEvent()
    data class ResetTo(val screen: AppScreen) : NavEvent()
    data class ReplaceWith(val screen: AppScreen) : NavEvent()
    data class ReplaceAll(val screen: AppScreen) : NavEvent()
    object Back : NavEvent()
    object PopToRoot : NavEvent()
}