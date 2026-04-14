package com.workfort.pstuian.ui.facultypicker

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerNavigationState
import com.workfort.pstuian.ui.facultypicker.state.FacultyPickerUiState
import kotlinx.coroutines.launch

class FacultyPickerViewModel(
    private val selectionMode: FacultySelectionMode,
    private val selectedFacultyId: Int,
    private val selectedBatchId: Int,
    private val facultyRepo: FacultyRepositoryImpl,
    private val stateMachine: FacultyPickerUiStateMachine,
) : UiStateMachineViewModel<FacultyPickerUiState>(stateMachine) {

    companion object {
        const val INVALID_ID = -1
    }

    private var cache = initializeCache()

    override fun onUiReady() {
        loadInitialData()
    }

    fun navigationConsumed() = stateMachine.navigateTo(null)

    fun onClickBack() = stateMachine.navigateTo(
        FacultyPickerNavigationState.GoBack(
            selectedFacultyId = null,
            selectedBatchId = null,
        )
    )

    fun onClickFaculty(faculty: FacultyEntity) {
        cache = cache.copy(selectedFacultyId = faculty.id)
        when (selectionMode) {
            FacultySelectionMode.FACULTY -> {
                stateMachine.navigateTo(
                    FacultyPickerNavigationState.GoBack(
                        selectedFacultyId = cache.selectedFacultyId,
                        selectedBatchId = null,
                    ),
                )
            }
            FacultySelectionMode.BATCH,
            FacultySelectionMode.BOTH, -> loadBatches()
            FacultySelectionMode.NONE -> Unit
        }
    }

    fun onClickBatch(batch: BatchEntity) {
        cache = cache.copy(selectedBatchId = batch.id)
        stateMachine.navigateTo(
            FacultyPickerNavigationState.GoBack(
                selectedFacultyId = cache.selectedFacultyId,
                selectedBatchId = cache.selectedBatchId,
            ),
        )
    }

    fun onClickChangeFaculty() {
        stateMachine.updatePanelState(
            FacultyPickerUiState.PanelState.SelectFaculty(
                currentSelection = cache.faculties.firstOrNull {
                    it.id == cache.selectedFacultyId
                },
                faculties = cache.faculties,
            ),
        )
    }

    fun loadInitialData() {
        stateMachine.updateLoading(true)
        cache = initializeCache()
        viewModelScope.launch {
            runCatching {
                val faculties = facultyRepo.getFaculties(forceRefresh = false)
                cache = cache.copy(faculties = ArrayList(faculties))
                when (selectionMode) {
                    FacultySelectionMode.FACULTY,
                    FacultySelectionMode.BOTH -> {
                        stateMachine.updatePanelState(
                            FacultyPickerUiState.PanelState.SelectFaculty(
                                currentSelection = cache.faculties.firstOrNull {
                                    it.id == cache.selectedFacultyId
                                },
                                faculties = cache.faculties,
                            ),
                        )
                    }
                    FacultySelectionMode.BATCH -> loadBatches()
                    FacultySelectionMode.NONE -> Unit
                }
            }.onFailure {
                val message = it.message ?: "Failed to load data"
                stateMachine.updateError(message)
            }
        }
    }

    private fun loadBatches() {
        if (cache.selectedFacultyId == INVALID_ID) {
            stateMachine.updateError("Invalid faculty")
            return
        }
        viewModelScope.launch {
            runCatching {
                val batches = facultyRepo.getBatches(cache.selectedFacultyId)
                cache = cache.copy(batches = ArrayList(batches))
                stateMachine.updatePanelState(
                    FacultyPickerUiState.PanelState.SelectBatch(
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
                stateMachine.updateError(message)
            }
        }
    }

    private data class Cache(
        val selectedFacultyId: Int,
        val selectedBatchId: Int,
        val faculties: ArrayList<FacultyEntity>,
        val batches: ArrayList<BatchEntity>,
    )

    private fun initializeCache() = Cache(
        selectedFacultyId = selectedFacultyId,
        selectedBatchId = selectedBatchId,
        faculties = arrayListOf(),
        batches = arrayListOf(),
    )
}