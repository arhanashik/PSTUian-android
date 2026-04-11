package com.workfort.pstuian.reducer.ui.blooddonationrequestlist

import com.workfort.pstuian.reducer.service.StateReducer


class BloodDonationRequestListScreenStateReducer : StateReducer<BloodDonationRequestListScreenState, BloodDonationRequestListScreenStateUpdate> {
 override val initial: BloodDonationRequestListScreenState
  get() = BloodDonationRequestListScreenState()
}