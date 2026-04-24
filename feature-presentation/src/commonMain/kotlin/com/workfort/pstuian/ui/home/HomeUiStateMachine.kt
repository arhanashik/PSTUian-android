package com.workfort.pstuian.ui.home

import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.Slider
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeUiStateMachine : UiStateMachine<HomeUiState> {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.None)
    override val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private fun updateUiState(updater: HomeUiState.() -> HomeUiState) = _uiState.update(updater)

    fun setInitialContent(isSignedInUser: Boolean) = updateUiState {
        HomeUiState.Content(isSignedInUser)
    }

    private fun updateContent(
        updater: HomeUiState.Content.() -> HomeUiState.Content,
    ) = updateUiState {
        if (this is HomeUiState.Content) {
            updater()
        } else this
    }

    fun updateSignedInState(isSignedInUser: Boolean) = updateContent {
        copy(isSignedInUser = isSignedInUser)
    }

    fun showSliderLoading() = updateContent {
        copy(sliderState = HomeUiState.SliderState.Loading)
    }

    fun showSliders(sliders: List<Slider>, scrollPosition: Int = 0) = updateContent {
        copy(sliderState = HomeUiState.SliderState.Available(sliders, scrollPosition))
    }

    fun updateSliderPosition(position: Int) = updateContent {
        if (sliderState is HomeUiState.SliderState.Available) {
            copy(sliderState = sliderState.copy(scrollPosition = position))
        } else {
            this
        }
    }

    fun showSliderError(message: String) = updateContent {
        copy(sliderState = HomeUiState.SliderState.Error(message))
    }

    fun showFacultyLoading() = updateContent {
        copy(facultyState = HomeUiState.FacultyState.Loading)
    }

    fun showFaculties(faculties: List<Faculty>) = updateContent {
        copy(facultyState = HomeUiState.FacultyState.Available(faculties))
    }

    fun showFacultyError(message: String) = updateContent {
        copy(facultyState = HomeUiState.FacultyState.Error(message))
    }
}
