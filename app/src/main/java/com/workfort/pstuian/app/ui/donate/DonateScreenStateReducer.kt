package com.workfort.pstuian.app.ui.donate

import com.workfort.pstuian.view.service.StateReducer


class DonateScreenStateReducer : StateReducer<DonateScreenState, DonateScreenStateUpdate> {
 override val initial: DonateScreenState
  get() = DonateScreenState()
}