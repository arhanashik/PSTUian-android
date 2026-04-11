package com.workfort.pstuian.app.ui.donors

import com.workfort.pstuian.reducer.service.StateReducer


class DonorsScreenStateReducer : StateReducer<DonorsScreenState, DonorsScreenStateUpdate> {
 override val initial: DonorsScreenState
  get() = DonorsScreenState()
}