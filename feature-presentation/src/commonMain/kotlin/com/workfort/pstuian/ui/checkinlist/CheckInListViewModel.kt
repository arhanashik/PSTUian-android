package com.workfort.pstuian.ui.checkinlist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.model.SharedScreenData
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
    private val uiStateMachine: CheckInListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CheckInListUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<CheckInListMessageState?>(null)
    val message: StateFlow<CheckInListMessageState?> = _message

    private val _navigation = MutableStateFlow<CheckInListNavigationState?>(null)
    val navigation: StateFlow<CheckInListNavigationState?> = _navigation

    private var checkInListPage = 1
    private var hasMoreCheckInListData = true
    private val checkInListCache = mutableListOf<CheckIn>()
    private var checkInLocationCache: CheckInLocation? = null

    private fun getLastCheckInLocationId(): Int {
        return sharedPrefRepository.getInt(
            key = SharedPrefKey.LAST_SHOWN_CHECK_IN_LOCATION_ID,
            defaultValue = NetworkConst.Params.CheckInLocation.MAIN_CAMPUS,
        )
    }

    override fun onUiReady() {
        loadInitialData()
    }

    fun onUiEvent(event: CheckInListUiEvent) {
        when (event) {
            is CheckInListUiEvent.OnClickBack -> onClickBack()
            is CheckInListUiEvent.OnClickItem -> onClickItem(event.item)
            is CheckInListUiEvent.OnClickCall -> onClickCall(event.phoneNumber)
            is CheckInListUiEvent.OnClickChangeLocation -> onClickChangeLocation()
            is CheckInListUiEvent.OnClickCheckIn -> onClickCheckIn()
            is CheckInListUiEvent.OnRefresh -> loadCheckInList(refresh = true)
            is CheckInListUiEvent.OnLoadMore -> loadCheckInList(refresh = false)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { CheckInListNavigationState.GoBack }
    }

    private fun onClickItem(item: CheckIn) {
        val userType = UserType.fromType(item.userType) ?: return
        _navigation.update {
            CheckInListNavigationState.ProfileScreen(
                userId = item.userId,
                userType = userType,
            )
        }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update { CheckInListMessageState.Call(phoneNumber) }
    }

    private var isLocationPickerForCheckIn = false
    private fun onClickChangeLocation() {
        isLocationPickerForCheckIn = false
        _navigation.update { CheckInListNavigationState.LocationPickerScreen }
    }

    private fun onClickCheckIn() {
        isLocationPickerForCheckIn = true
        _navigation.update { CheckInListNavigationState.LocationPickerScreen }
    }

    fun onChangeLocation(locationId: Int) {
        if (isLocationPickerForCheckIn) {
            loadAndConfirmCheckIn(locationId)
            return
        }
        checkInLocationCache = null
        sharedPrefRepository.putInt(SharedPrefKey.LAST_SHOWN_CHECK_IN_LOCATION_ID, locationId)
        loadCheckInList(refresh = true)
    }

    private fun loadInitialData() {
        uiStateMachine.showOperationLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            val locationId = getLastCheckInLocationId()
            checkInLocationRepo.get(locationId)
                .onSuccess {
                    checkInLocationCache = it
                    uiStateMachine.showInitialContent(it)
                    loadCheckInList(refresh = true)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load check in location data"
                    uiStateMachine.showError(message)
                }
        }
    }

    private fun loadCheckInList(refresh: Boolean) {
        if (refresh) {
            checkInListPage = 1
            hasMoreCheckInListData = true
            checkInListCache.clear()
            uiStateMachine.showCheckInList(checkInListCache)
        } else if (!hasMoreCheckInListData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            val locationId = getLastCheckInLocationId()
            checkInRepo.getAll(locationId, checkInListPage)
                .onSuccess { checkInList ->
                    uiStateMachine.showContentLoading(isLoading = true)
                    if (checkInList.isEmpty()) {
                        hasMoreCheckInListData = false
                    } else {
                        checkInListPage++
                    }
                    checkInListCache.addAll(checkInList)
                    uiStateMachine.showCheckInList(checkInListCache)
                }
                .onFailure {
                    uiStateMachine.showContentLoading(isLoading = true)
                    val message = it.message ?: "Failed to load data"
                    _message.update { CheckInListMessageState.Error(message) }
                }
        }
    }

    private fun loadAndConfirmCheckIn(locationId: Int) {
        // TODO show loading
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            checkInLocationRepo.get(locationId)
                .onSuccess { location ->
                    // TODO hide loading
                    _message.update { CheckInListMessageState.ConfirmCheckIn(location) }
                }
                .onFailure {
                    // TODO hide loading
                    val message = it.message ?: "Check in failed. Please try again."
                    _message.update { CheckInListMessageState.Error(message) }
                }
        }
    }

    fun checkIn(locationId: Int) {
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        // TODO show loading
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            checkInRepo.checkIn(userId, userType, locationId)
                .onSuccess {
                    // TODO hide loading
                    _message.update { CheckInListMessageState.Success("Checked in successfully!") }
                    checkInLocationCache = null
                    sharedPrefRepository.putInt(SharedPrefKey.LAST_SHOWN_CHECK_IN_LOCATION_ID, locationId)
                    loadInitialData()
                }
                .onFailure {
                    // TODO hide loading
                    val message = it.message ?: "Check in failed. Please try again."
                    _message.update { CheckInListMessageState.Error(message) }
                }
        }
    }
}
