package com.workfort.pstuian.app.ui.home

import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.view.service.StateUpdate


sealed interface HomeScreenStateUpdate : StateUpdate<HomeScreenState> {

    data class UpdateSliderPosition(val position: Int) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            val state = when (displayState.sliderState) {
                is HomeScreenState.DisplayState.SliderState.Available -> {
                    displayState.sliderState.copy(scrollPosition = position)
                }
                else -> displayState.sliderState
            }
            copy(displayState = displayState.copy(sliderState = state))
        }
    }

    data object ProfileLoading : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = HomeScreenState.DisplayState.ProfileState.Loading
                )
            )
        }
    }

    data class ProfileLoaded(val user: Any) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = HomeScreenState.DisplayState.ProfileState.Available(user)
                )
            )
        }
    }

    data class ProfileLoadFailed(val message: String) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    profileState = HomeScreenState.DisplayState.ProfileState.Error(message)
                )
            )
        }
    }

    data object SliderLoading : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    sliderState = HomeScreenState.DisplayState.SliderState.Loading
                )
            )
        }
    }

    data class SliderLoaded(val sliders: List<SliderEntity>) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    sliderState = HomeScreenState.DisplayState.SliderState.Available(
                        sliders = sliders,
                        scrollPosition = 0,
                    )
                )
            )
        }
    }

    data class SliderLoadFailed(val message: String) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    sliderState = HomeScreenState.DisplayState.SliderState.Error(message)
                )
            )
        }
    }

    data object FacultyLoading : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    facultyState = HomeScreenState.DisplayState.FacultyState.Loading
                )
            )
        }
    }

    data class FacultyLoaded(val faculties: List<FacultyEntity>) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    facultyState = HomeScreenState.DisplayState.FacultyState.Available(faculties)
                )
            )
        }
    }

    data class FacultyLoadFailed(val message: String) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    facultyState = HomeScreenState.DisplayState.FacultyState.Error(message)
                )
            )
        }
    }

    data class UpdateMessageState(
        val newState: HomeScreenState.DisplayState.MessageState
    ) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = newState))
        }
    }

    data object MessageConsumed : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(val newState: HomeScreenState.NavigationState) : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : HomeScreenStateUpdate {
        override fun invoke(oldState: HomeScreenState): HomeScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}