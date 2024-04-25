package com.workfort.pstuian.app.ui.home

import com.workfort.pstuian.view.service.StateReducer


class HomeScreenStateReducer : StateReducer<HomeScreenState, HomeScreenStateUpdate> {
 override val initial: HomeScreenState
  get() = HomeScreenState()
}