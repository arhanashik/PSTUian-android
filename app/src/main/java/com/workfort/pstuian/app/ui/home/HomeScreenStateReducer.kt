package com.workfort.pstuian.app.ui.home

import com.workfort.pstuian.reducer.service.StateReducer


class HomeScreenStateReducer : StateReducer<HomeScreenState, HomeScreenStateUpdate> {
 override val initial: HomeScreenState
  get() = HomeScreenState()
}