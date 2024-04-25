package com.workfort.pstuian.app.ui.checkinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.CheckInLocationRepository
import com.workfort.pstuian.repository.CheckInRepository
import com.workfort.pstuian.sharedpref.Prefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckInListViewModel(
    private val checkInRepo : CheckInRepository,
    private val checkInLocationRepo: CheckInLocationRepository,
    private val reducer: CheckInListScreenStateReducer,
) : ViewModel() {

    /**
     * Load default check in location - last selected check in location
     * If not selected yet show for Main Campus
     * */
    private fun getLastCheckInLocationId(): Int = Prefs.lastShownCheckInLocationId.let { locationId ->
        if(locationId == -1) NetworkConst.Params.CheckInLocation.MAIN_CAMPUS
        else locationId
    }

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<CheckInListScreenState> get() = _screenState

    private fun updateScreenState(update: CheckInListScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(CheckInListScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(CheckInListScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        CheckInListScreenStateUpdate.NavigateTo(CheckInListScreenState.NavigationState.GoBack)
    )

    fun onClickItem(item: CheckInEntity) {
        val userType = UserType.create(item.userType) ?: return
        updateScreenState(
            CheckInListScreenStateUpdate.NavigateTo(
                CheckInListScreenState.NavigationState.ProfileScreen(
                    userId = item.userId,
                    userType = userType,
                ),
            ),
        )
    }

    fun onClickCall(phoneNumber: String) = updateScreenState(
        CheckInListScreenStateUpdate.UpdateMessageState(
            CheckInListScreenState.DisplayState.MessageState.Call(phoneNumber),
        ),
    )

    fun onClickChangeLocation() {
        isLocationPickerForCheckIn = false
        updateScreenState(
            CheckInListScreenStateUpdate.NavigateTo(
                CheckInListScreenState.NavigationState.LocationPickerScreen,
            ),
        )
    }

    private var isLocationPickerForCheckIn = false
    fun onClickCheckIn() {
        isLocationPickerForCheckIn = true
        updateScreenState(
            CheckInListScreenStateUpdate.NavigateTo(
                CheckInListScreenState.NavigationState.LocationPickerScreen,
            ),
        )
    }

    fun onChangeLocation(locationId: Int) {
        if (isLocationPickerForCheckIn) {
            loadAndConfirmCheckIn(locationId)
            return
        }
        checkInLocationCache = null
        Prefs.lastShownCheckInLocationId = locationId
        loadCheckInList(refresh = true)
    }

    private fun isCheckInListLoading(): Boolean {
        val state = _screenState.value.displayState.checkInListState
        return if (state is CheckInListScreenState.DisplayState.CheckInListState.Available) {
            state.isLoading
        } else {
            false
        }
    }

    private var checkInLocationCache: CheckInLocationEntity? = null
    private fun loadCheckInLocation() {
        viewModelScope.launch {
            runCatching {
                val locationId = getLastCheckInLocationId()
                checkInLocationCache = checkInLocationRepo.get(locationId)
                updateScreenState(
                    CheckInListScreenStateUpdate.ShowCheckInListLocation(
                        checkInLocation = checkInLocationCache,
                    )
                )
            }
        }
    }

    private var checkInListPage = 0
    private var endOfCheckInListData = false
    private val checkInListCache = arrayListOf<CheckInEntity>()
    fun loadCheckInList(refresh: Boolean) {
        if (isCheckInListLoading() || (refresh.not() && endOfCheckInListData)) {
            return
        }
        updateScreenState(
            CheckInListScreenStateUpdate.ShowCheckInListList(
                checkInList = checkInListCache,
                isLoading = true,
            )
        )
        if (refresh) {
            checkInListPage = 0
            checkInListCache.clear()
        }
        if (checkInLocationCache == null) {
            loadCheckInLocation()
        }
        checkInListPage += 1
        viewModelScope.launch {
            runCatching {
                val locationId = getLastCheckInLocationId()
                val list = checkInRepo.getAll(locationId, checkInListPage)
                if (list.isEmpty()) {
                    endOfCheckInListData = true
                } else {
                    checkInListCache.addAll(list)
                }
                updateScreenState(
                    CheckInListScreenStateUpdate.ShowCheckInListList(
                        checkInList = checkInListCache,
                        isLoading = false,
                    )
                )
            }.onFailure {
                endOfCheckInListData = true
                val message = it.message ?: "Failed to load data"
                val state = if (checkInListCache.isEmpty()) {
                    CheckInListScreenStateUpdate.CheckInListListLoadFailed(message)
                } else {
                    CheckInListScreenStateUpdate.ShowCheckInListList(
                        checkInList = checkInListCache,
                        isLoading = false,
                    )
                }
                updateScreenState(state)
            }
        }
    }

    private fun loadAndConfirmCheckIn(locationId: Int) {
        updateScreenState(
            CheckInListScreenStateUpdate.UpdateMessageState(
                CheckInListScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                checkInLocationRepo.get(locationId)
            }.onSuccess {
                updateScreenState(
                    CheckInListScreenStateUpdate.UpdateMessageState(
                        CheckInListScreenState.DisplayState.MessageState.ConfirmCheckIn(it),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Check in failed. Please try again."
                updateScreenState(
                    CheckInListScreenStateUpdate.UpdateMessageState(
                        CheckInListScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    fun checkIn(locationId: Int) {
        updateScreenState(
            CheckInListScreenStateUpdate.UpdateMessageState(
                CheckInListScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                checkInRepo.checkIn(locationId)
            }.onSuccess {
                updateScreenState(
                    CheckInListScreenStateUpdate.UpdateMessageState(
                        CheckInListScreenState.DisplayState.MessageState.Success(
                            "Checked in successfully!"
                        ),
                    ),
                )
                checkInLocationCache = null
                Prefs.lastShownCheckInLocationId = locationId
                loadCheckInList(refresh = true)
            }.onFailure {
                val message = it.message ?: "Check in failed. Please try again."
                updateScreenState(
                    CheckInListScreenStateUpdate.UpdateMessageState(
                        CheckInListScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}