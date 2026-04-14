package com.workfort.pstuian.common.navigation

sealed class NavEvent {
    data class Navigate(val screen: AppScreen) : NavEvent()
    object Back : NavEvent()
    object PopToRoot : NavEvent()
}