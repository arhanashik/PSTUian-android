package com.workfort.pstuian.reducer.ui.donate

import com.workfort.pstuian.reducer.service.StateReducer


class DonateScreenStateReducer : StateReducer<DonateScreenState, DonateScreenStateUpdate> {
 override val initial: DonateScreenState
  get() = DonateScreenState()
}