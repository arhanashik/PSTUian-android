package com.workfort.pstuian.ui.home.state

import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.Slider

sealed interface HomeUiState {

    data object None : HomeUiState

    data class Content(
        val profileImageUrl: String? = null,
        val sliderState: SliderState = SliderState.None,
        val facultyState: FacultyState = FacultyState.None,
    ) : HomeUiState

    sealed interface SliderState {
        data object None : SliderState
        data object Loading : SliderState
        data class Available(
            val sliders: List<Slider>,
            val scrollPosition: Int = 0,
        ) : SliderState
        data class Error(val message: String) : SliderState
    }

    sealed interface FacultyState {
        data object None : FacultyState
        data object Loading : FacultyState
        data class Available(val faculties: List<FacultyEntity>) : FacultyState
        data class Error(val message: String) : FacultyState
    }
}
