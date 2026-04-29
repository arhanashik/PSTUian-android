package com.workfort.pstuian.ui.checkinlist

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
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkinlist.state.CheckInListMessageState
import com.workfort.pstuian.ui.checkinlist.state.CheckInListNavigationState
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiEvent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CheckInListViewModel(
    private val checkInRepo: CheckInRepository,
    private val checkInLocationRepo: CheckInLocationRepository,
    private val sharedPrefRepository: SharedPrefRepository,
    private val sharedScreenData: SharedScreenData,
    private val checkInDisplayDataMapper: CheckInDisplayDataMapper,
    private val uiStateMachine: CheckInListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CheckInListUiState>(uiStateMachine) {

    private val currentUserId = sharedScreenData.getCurrentUser()?.userId ?: 0

    private val _message = MutableStateFlow<CheckInListMessageState?>(null)
    val message: StateFlow<CheckInListMessageState?> = _message

    private val _navigation = MutableStateFlow<CheckInListNavigationState?>(null)
    val navigation: StateFlow<CheckInListNavigationState?> = _navigation

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
        loadCheckInLocations(refresh = true, loadCheckInListAfter = true)
    }

    fun onUiEvent(event: CheckInListUiEvent) {
        when (event) {
            is CheckInListUiEvent.BackClicked -> _navigation.update { CheckInListNavigationState.GoBack }
            is CheckInListUiEvent.CheckInItemClicked -> onClickCheckInItem(event.item)
            is CheckInListUiEvent.LocationSelected -> onSelectLocation(event.locationId)
            is CheckInListUiEvent.CallClicked -> onClickCall(event.phoneNumber)
            is CheckInListUiEvent.CheckInClicked -> onClickCheckIn(event.selectedLocationId)
            is CheckInListUiEvent.OnLoadMoreLocations -> loadCheckInLocations(refresh = false)
            is CheckInListUiEvent.OnLoadMoreCheckIn -> loadCheckInList(event.locationId, refresh = false)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onSelectLocation(locationId: Int) {
        uiStateMachine.updatedSelectedCheckInLocationId(locationId)
        loadCheckInList(locationId, refresh = true)
    }

    private fun onClickCheckInItem(item: CheckInDisplayData) {
        val userType = UserType.fromType(item.checkIn.userType) ?: return
        _navigation.update {
            CheckInListNavigationState.ProfileScreen(
                userId = item.checkIn.userId,
                userType = userType,
            )
        }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update { CheckInListMessageState.Call(phoneNumber) }
    }

    private fun onClickCheckIn(selectedLocationId: Int) {
        _message.update {
            CheckInListMessageState.CheckInLocationSelection(
                checkInLocationsCache,
                selectedLocationId,
            ) { location ->
                _message.update {
                    CheckInListMessageState.ConfirmCheckIn(location) {
                        checkIn(location.id)
                    }
                }
            }
        }
    }

    private fun loadCheckInLocations(refresh: Boolean, loadCheckInListAfter: Boolean = false) {
        if (isLoadingCheckInLocations) return
        if (refresh) {
            checkInLocationsPage = 1
            hasMoreCheckInLocationsData = true
            checkInLocationsCache.clear()
        } else if (!hasMoreCheckInLocationsData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            isLoadingCheckInLocations = true
            uiStateMachine.showLocationListLoading(isLoading = true)
            checkInLocationRepo.getAll(checkInLocationsPage, refresh)
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
                    if (loadCheckInListAfter) {
                        loadCheckInList(selectedLocationId, refresh = true)
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

    private fun loadCheckInList(locationId: Int, refresh: Boolean) {
        if (isLoadingCheckIns) return
        if (refresh) {
            checkInsPage = 1
            hasMoreCheckInsData = true
            checkInsCache.clear()
            uiStateMachine.showCheckInList(currentUserCheckIn = null, otherCheckIns = emptyList())
        } else if (!hasMoreCheckInsData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            isLoadingCheckIns = true
            uiStateMachine.showCheckInListLoading(isLoading = true)
            checkInRepo.getAll(locationId, checkInsPage)
                .onSuccess { checkInList ->
                    if (checkInList.isEmpty()) {
                        hasMoreCheckInsData = false
                    } else {
                        checkInsPage++
                    }
                    val displayDataList = checkInDisplayDataMapper.map(checkInList, currentUserId)
                    checkInsCache.addAll(displayDataList)

                    uiStateMachine.showCheckInListLoading(isLoading = false)
                    val currentUserCheckIn = checkInsCache.firstOrNull { it.checkIn.userId == currentUserId }
                    val otherCheckIns = checkInsCache.filter { it.checkIn.userId != currentUserId }
                    uiStateMachine.showCheckInList(currentUserCheckIn, otherCheckIns)
                }
                .onFailure {
                    uiStateMachine.showCheckInListLoading(isLoading = false)
                    val message = it.message ?: "Failed to load data"
                    _message.update { CheckInListMessageState.Error(message) }
                }
            isLoadingCheckIns = false
        }
    }

    private fun checkIn(locationId: Int) {
        val userType = sharedScreenData.getCurrentUserType() ?: return

        _message.update { CheckInListMessageState.Loading() }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            checkInRepo.checkIn(locationId, currentUserId, userType)
                .onSuccess {
                    onMessageHandled()
                    _message.update { CheckInListMessageState.ShowSnackBar("Checked in successfully!") }
                    checkInRepo.clearCache()
                    loadCheckInList(locationId, refresh = true)
                }
                .onFailure {
                    onMessageHandled()
                    val message = it.message ?: "Check in failed. Please try again."
                    _message.update { CheckInListMessageState.Error(message) }
                }
        }
    }
}
