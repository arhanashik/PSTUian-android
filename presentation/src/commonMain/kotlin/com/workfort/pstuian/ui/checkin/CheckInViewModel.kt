package com.workfort.pstuian.ui.checkin

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.checkin.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkin.state.CheckInMessageState
import com.workfort.pstuian.ui.checkin.state.CheckInNavigationState
import com.workfort.pstuian.ui.checkin.state.CheckInUiEvent
import com.workfort.pstuian.ui.checkin.state.CheckInUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CheckInViewModel(
    private val checkInRepo: CheckInRepository,
    private val checkInLocationRepo: CheckInLocationRepository,
    private val sharedScreenData: SharedScreenData,
    private val checkInDisplayDataMapper: CheckInDisplayDataMapper,
    private val uiStateMachine: CheckInUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CheckInUiState>(uiStateMachine) {

    private val currentUserId = sharedScreenData.getCurrentUser()?.userId ?: 0

    private val _message = MutableStateFlow<CheckInMessageState?>(null)
    val message: StateFlow<CheckInMessageState?> = _message

    private val _navigation = MutableStateFlow<CheckInNavigationState?>(null)
    val navigation: StateFlow<CheckInNavigationState?> = _navigation

    // cache data
    private var checkInLocationsPage = 1
    private var hasMoreCheckInLocationsData = true
    private var checkInLocationsCache = mutableListOf<CheckInLocation>()
    private var isLoadingCheckInLocations = false

    private var checkInsPage = 1
    private var hasMoreCheckInsData = true
    private val checkInsCache = mutableListOf<CheckInDisplayData>()
    private var isLoadingCheckIns = false

    override fun onUiReady() {
        uiStateMachine.showOperationLoading()
        loadCheckInLocations(forceRefresh = false, loadCheckInAfter = true)
    }

    fun onUiEvent(event: CheckInUiEvent) {
        when (event) {
            is CheckInUiEvent.BackClicked -> _navigation.update { CheckInNavigationState.GoBack }
            is CheckInUiEvent.CheckInItemClicked -> onClickCheckInItem(event.item)
            is CheckInUiEvent.LocationSelected -> onSelectLocation(event.locationId)
            is CheckInUiEvent.CallClicked -> onClickCall(event.phoneNumber)
            is CheckInUiEvent.CheckInClicked -> onClickCheckIn(event.selectedLocationId)
            is CheckInUiEvent.OnLoadMoreLocations -> loadCheckInLocations(forceRefresh = false)
            is CheckInUiEvent.OnLoadMoreCheckIn -> loadCheckIn(event.locationId, forceRefresh = false)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onSelectLocation(locationId: Int) {
        uiStateMachine.updatedSelectedCheckInLocationId(locationId)
        loadCheckIn(locationId, forceRefresh = true)
    }

    private fun onClickCheckInItem(item: CheckInDisplayData) {
        val userType = UserType.fromType(item.checkIn.userType) ?: return
        _navigation.update {
            CheckInNavigationState.ProfileScreen(
                userId = item.checkIn.userId,
                userType = userType,
            )
        }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update { CheckInMessageState.Call(phoneNumber) }
    }

    private fun onClickCheckIn(selectedLocationId: Int) {
        _message.update {
            CheckInMessageState.CheckInLocationSelection(
                checkInLocationsCache,
                selectedLocationId,
            ) { location ->
                _message.update {
                    CheckInMessageState.ConfirmCheckIn(location) { checkIn(location.id) }
                }
            }
        }
    }

    private fun loadCheckInLocations(forceRefresh: Boolean, loadCheckInAfter: Boolean = false) {
        if (isLoadingCheckInLocations) return
        if (forceRefresh) {
            checkInLocationsPage = 1
            hasMoreCheckInLocationsData = true
            checkInLocationsCache.clear()
        } else if (!hasMoreCheckInLocationsData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            isLoadingCheckInLocations = true
            uiStateMachine.showLocationListLoading(isLoading = true)
            checkInLocationRepo.getAll(checkInLocationsPage, forceRefresh)
                .onSuccess { locations ->
                    if (locations.isEmpty()) {
                        hasMoreCheckInLocationsData = false
                    } else {
                        checkInLocationsPage++
                    }
                    checkInLocationsCache.addAll(locations)

                    val selectedLocationId = getOwnCheckInLocationId()
                    uiStateMachine.showInitialContent(
                        checkInLocations = checkInLocationsCache,
                        selectedLocationId = selectedLocationId,
                    )
                    uiStateMachine.showLocationListLoading(isLoading = false)
                    if (loadCheckInAfter) {
                        loadCheckIn(selectedLocationId, forceRefresh = true)
                    }
                }
                .onFailure {
                    uiStateMachine.showLocationListLoading(isLoading = false)
                    val message = it.message ?: "Failed to load check in location data"
                    uiStateMachine.showError(message)
                }
            isLoadingCheckInLocations = false
        }
    }

    private suspend fun getOwnCheckInLocationId(): Int {
        val defaultValue = NetworkConst.Params.CheckInLocation.MAIN_CAMPUS
        val userType = sharedScreenData.getCurrentUserType() ?: return defaultValue
        return checkInRepo.get(currentUserId, userType).getOrNull()?.locationId ?: defaultValue
    }

    private fun loadCheckIn(locationId: Int, forceRefresh: Boolean) {
        if (isLoadingCheckIns) return
        if (forceRefresh) {
            checkInsPage = 1
            hasMoreCheckInsData = true
            checkInsCache.clear()
            uiStateMachine.showCheckIn(currentUserCheckIn = null, otherCheckIns = emptyList())
        } else if (!hasMoreCheckInsData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            isLoadingCheckIns = true
            uiStateMachine.showCheckInLoading(isLoading = true)
            checkInRepo.getAll(locationId, checkInsPage, forceRefresh)
                .onSuccess { checkInList ->
                    if (checkInList.isEmpty()) {
                        hasMoreCheckInsData = false
                    } else {
                        checkInsPage++
                    }
                    val displayDataList = checkInDisplayDataMapper.map(checkInList, currentUserId)
                    checkInsCache.addAll(displayDataList)

                    uiStateMachine.showCheckInLoading(isLoading = false)
                    val currentUserCheckIn = checkInsCache.firstOrNull { it.checkIn.userId == currentUserId }
                    val otherCheckIns = checkInsCache.filter { it.checkIn.userId != currentUserId }
                    uiStateMachine.showCheckIn(currentUserCheckIn, otherCheckIns)
                }
                .onFailure {
                    uiStateMachine.showCheckInLoading(isLoading = false)
                    val message = it.message ?: "Failed to load data"
                    _message.update { CheckInMessageState.Error(message) }
                }
            isLoadingCheckIns = false
        }
    }

    private fun checkIn(locationId: Int) {
        val userType = sharedScreenData.getCurrentUserType() ?: return

        _message.update { CheckInMessageState.Loading() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            checkInRepo.checkIn(locationId, currentUserId, userType)
                .onSuccess {
                    onMessageHandled()
                    _message.update { CheckInMessageState.ShowSnackBar("Checked in successfully!") }
                    loadCheckIn(locationId, forceRefresh = true)
                }
                .onFailure {
                    onMessageHandled()
                    val message = it.message ?: "Check in failed. Please try again."
                    _message.update { CheckInMessageState.Error(message) }
                }
        }
    }
}
