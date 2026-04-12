package com.workfort.pstuian.reducer.ui.blooddonationrequestcreate

import com.workfort.pstuian.reducer.service.StateReducer


class BloodDonationRequestCreateScreenStateReducer : StateReducer<BloodDonationRequestCreateScreenState, BloodDonationRequestCreateScreenStateUpdate> {
 override val initial: BloodDonationRequestCreateScreenState
  get() = BloodDonationRequestCreateScreenState()
}