package com.workfort.pstuian.app.ui.common.facultypicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.repository.FacultyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FacultyPickerViewModel(
    private val selectionMode: FacultySelectionMode,
    private val selectedFacultyId: Int,
    private val selectedBatchId: Int,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: FacultyPickerScreenStateReducer,
) : ViewModel() {

    companion object {
        const val INVALID_ID = -1
    }

    private var cache = initializeCache()

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<FacultyPickerScreenState> get() = _screenState

    private fun updateScreenState(update: FacultyPickerScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun navigationConsumed() = updateScreenState(FacultyPickerScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        FacultyPickerScreenStateUpdate.NavigateTo(
            FacultyPickerScreenState.NavigationState.GoBack(
                selectedFacultyId = null,
                selectedBatchId = null,
            )
        )
    )

    fun onClickFaculty(faculty: FacultyEntity) {
        cache.copy(selectedFacultyId = faculty.id).update()
        when (selectionMode) {
            FacultySelectionMode.FACULTY -> {
                updateScreenState(
                    FacultyPickerScreenStateUpdate.NavigateTo(
                        FacultyPickerScreenState.NavigationState.GoBack(
                            selectedFacultyId = cache.selectedFacultyId,
                            selectedBatchId = null,
                        ),
                    ),
                )
            }
            FacultySelectionMode.BATCH,
            FacultySelectionMode.BOTH, -> loadBatches()
        }

    }

    fun onClickBatch(batch: BatchEntity) {
        cache.copy(selectedBatchId = batch.id).update()
        val newState = FacultyPickerScreenStateUpdate.NavigateTo(
            FacultyPickerScreenState.NavigationState.GoBack(
                selectedFacultyId = cache.selectedFacultyId,
                selectedBatchId = cache.selectedBatchId,
            ),
        )
        updateScreenState(newState)
    }

    fun onClickChangeFaculty() {
        updateScreenState(
            FacultyPickerScreenStateUpdate.ShowSelectFacultyPanel(
                currentSelection = cache.faculties.firstOrNull {
                    it.id == cache.selectedFacultyId
                },
                faculties = cache.faculties,
            ),
        )
    }

    fun loadInitialData() {
        updateScreenState(FacultyPickerScreenStateUpdate.UpdateLoading(true))
        cache = initializeCache()
        viewModelScope.launch {
            runCatching {
                val faculties = facultyRepo.getFaculties(forceRefresh = false)
                cache.copy(faculties = ArrayList(faculties)).update()
                when (selectionMode) {
                    FacultySelectionMode.FACULTY,
                    FacultySelectionMode.BOTH -> {
                        updateScreenState(
                            FacultyPickerScreenStateUpdate.ShowSelectFacultyPanel(
                                currentSelection = cache.faculties.firstOrNull {
                                    it.id == cache.selectedFacultyId
                                },
                                faculties = cache.faculties,
                            ),
                        )
                    }
                    FacultySelectionMode.BATCH -> loadBatches()
                }
            }.onFailure {
                val message = it.message ?: "Failed to load data"
                updateScreenState(FacultyPickerScreenStateUpdate.DataLoadFailed(message))
            }
        }
    }

    private fun loadBatches() {
        if (cache.selectedFacultyId == INVALID_ID) {
            updateScreenState(FacultyPickerScreenStateUpdate.DataLoadFailed("Invalid faculty"))
            return
        }
        viewModelScope.launch {
            runCatching {
                val batches = facultyRepo.getBatches(cache.selectedFacultyId)
                cache.copy(batches = ArrayList(batches)).update()
                updateScreenState(
                    FacultyPickerScreenStateUpdate.ShowSelectBatchPanel(
                        selectedFaculty = cache.faculties.first {
                            it.id == cache.selectedFacultyId
                        },
                        currentSelection = cache.batches.firstOrNull {
                            it.id == cache.selectedBatchId
                        },
                        batches = cache.batches,
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to load data"
                updateScreenState(FacultyPickerScreenStateUpdate.DataLoadFailed(message))
            }
        }
    }

    data class Cache(
        val selectedFacultyId: Int,
        val selectedBatchId: Int,
        val faculties: ArrayList<FacultyEntity>,
        val batches: ArrayList<BatchEntity>,
    )

    private fun Cache.update() {
        cache = this
    }

    private fun initializeCache() = Cache(
        selectedFacultyId = selectedFacultyId,
        selectedBatchId = selectedBatchId,
        faculties = arrayListOf(),
        batches = arrayListOf(),
    )
}
