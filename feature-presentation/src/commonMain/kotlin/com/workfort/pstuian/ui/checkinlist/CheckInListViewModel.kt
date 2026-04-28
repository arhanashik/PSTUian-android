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
    private var endOfCheckInListData = false
    private val checkInListCache = arrayListOf<CheckIn>()
    private var checkInLocationCache: CheckInLocation? = null

    private fun getLastCheckInLocationId(): Int {
        return sharedPrefRepository.getInt(
            key = SharedPrefKey.LAST_SHOWN_CHECK_IN_LOCATION_ID,
            defaultValue = NetworkConst.Params.CheckInLocation.MAIN_CAMPUS,
        )
    }

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
        loadCheckInList(refresh = true)
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

    private fun loadCheckInLocation() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                val locationId = getLastCheckInLocationId()
                checkInLocationCache = checkInLocationRepo.get(locationId)
                uiStateMachine.showCheckInListLocation(checkInLocationCache)
            }
        }
    }

    private fun isCheckInListLoading(): Boolean {
        val state = uiState.value
        return if (state is CheckInListUiState.Content) {
            state.isLoading
        } else {
            false
        }
    }

    private fun loadCheckInList(refresh: Boolean) {
        if (refresh) {
            checkInListPage = 1
            checkInListCache.clear()
            endOfCheckInListData = false
        }

        if (isCheckInListLoading() || endOfCheckInListData) {
            return
        }

        uiStateMachine.showCheckInList(checkInListCache.toList(), isLoading = true)

        if (checkInLocationCache == null) {
            loadCheckInLocation()
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            val locationId = getLastCheckInLocationId()
            checkInRepo.getAll(locationId, checkInListPage)
                .onSuccess { checkInList ->
                    checkInListPage++
                    if (checkInList.isEmpty()) {
                        endOfCheckInListData = true
                    } else {
                        checkInListCache.addAll(checkInList)
                    }
                    uiStateMachine.showCheckInList(checkInListCache.toList(), isLoading = false)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    if (checkInListCache.isEmpty()) {
                        uiStateMachine.showError(message)
                    } else {
                        uiStateMachine.showCheckInList(checkInListCache.toList(), isLoading = false)
                        _message.update { CheckInListMessageState.Error(message) }
                    }
                }
        }
    }

    private fun loadAndConfirmCheckIn(locationId: Int) {
        uiStateMachine.showOperationLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                checkInLocationRepo.get(locationId)
            }.onSuccess {
                uiStateMachine.showOperationLoading(false)
                val location = it
                _message.update { CheckInListMessageState.ConfirmCheckIn(location) }
            }.onFailure {
                uiStateMachine.showOperationLoading(false)
                val message = it.message ?: "Check in failed. Please try again."
                _message.update { CheckInListMessageState.Error(message) }
            }
        }
    }

    fun checkIn(locationId: Int) {
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        uiStateMachine.showOperationLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                checkInRepo.checkIn(userId, userType, locationId)
            }.onSuccess {
                uiStateMachine.showOperationLoading(false)
                _message.update {
                    CheckInListMessageState.Success("Checked in successfully!")
                }
                checkInLocationCache = null
                sharedPrefRepository.putInt(SharedPrefKey.LAST_SHOWN_CHECK_IN_LOCATION_ID, locationId)
                loadCheckInList(refresh = true)
            }.onFailure {
                uiStateMachine.showOperationLoading(false)
                val message = it.message ?: "Check in failed. Please try again."
                _message.update { CheckInListMessageState.Error(message) }
            }
        }
    }
}
