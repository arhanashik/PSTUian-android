package com.workfort.pstuian.app.ui.blooddonationrequestlist

import com.workfort.pstuian.reducer.service.StateReducer


class BloodDonationRequestListScreenStateReducer : StateReducer<BloodDonationRequestListScreenState, BloodDonationRequestListScreenStateUpdate> {
 override val initial: BloodDonationRequestListScreenState
  get() = BloodDonationRequestListScreenState()
}