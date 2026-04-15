package com.workfort.pstuian.common.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigator {
    private val _events = MutableSharedFlow<NavEvent>()
    val events = _events.asSharedFlow()

    suspend fun navigateTo(screen: AppScreen) {
        _events.emit(NavEvent.Navigate(screen))
    }

    suspend fun resetTo(screen: AppScreen) {
        _events.emit(NavEvent.Navigate(screen))
    }

    suspend fun goBack() {
        _events.emit(NavEvent.Back)
    }

    suspend fun popToRoot() {
        _events.emit(NavEvent.PopToRoot)
    }
}