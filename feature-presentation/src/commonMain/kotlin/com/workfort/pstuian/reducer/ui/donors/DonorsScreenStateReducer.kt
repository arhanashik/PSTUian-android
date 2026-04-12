package com.workfort.pstuian.reducer.ui.donors

import com.workfort.pstuian.reducer.service.StateReducer


class DonorsScreenStateReducer : StateReducer<DonorsScreenState, DonorsScreenStateUpdate> {
 override val initial: DonorsScreenState
  get() = DonorsScreenState()
}