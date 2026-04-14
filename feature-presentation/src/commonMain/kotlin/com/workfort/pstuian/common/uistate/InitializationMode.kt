package com.workfort.pstuian.common.uistate

import kotlinx.coroutines.flow.SharingStarted

/**
 * @property Manual Initialization method must be called from the View manually.
 * Usually called from a `LaunchedEffect` or `onViewCreated`
 * @property JustOnce Initialization method will be called only once when the view
 * is created for the first time. If the view component is recreated, the initialization
 * method will not be called.
 * @property Custom Initialization method will be called automatically according to the
 * provided [SharingStarted] value. This allows more flexible initialization behaviors,
 * like calling the initialization method in the ViewModel as soon as the user leaves the
 * screen, if the screen is in the background for more than xx seconds and so on.
 */
sealed interface InitializationMode {
    data object Manual: InitializationMode
    data object JustOnce: InitializationMode
    data class Custom(val sharingStartedMode: SharingStarted): InitializationMode
}