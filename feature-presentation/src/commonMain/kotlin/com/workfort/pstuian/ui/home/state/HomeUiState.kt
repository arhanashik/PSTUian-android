package com.workfort.pstuian.ui.home.state

import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity

sealed interface HomeUiState {
    data object None : HomeUiState
    data class Content(
        val profileState: ProfileState = ProfileState.None,
        val sliderState: SliderState = SliderState.None,
        val facultyState: FacultyState = FacultyState.None,
    ) : HomeUiState

    sealed interface ProfileState {
        data object None : ProfileState
        data object Loading : ProfileState
        data class Available(val user: Any) : ProfileState
        data class Error(val message: String) : ProfileState
    }

    sealed interface SliderState {
        data object None : SliderState
        data object Loading : SliderState
        data class Available(
            val sliders: List<SliderEntity>,
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
