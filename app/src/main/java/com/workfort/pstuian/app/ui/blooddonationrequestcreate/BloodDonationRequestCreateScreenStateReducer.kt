package com.workfort.pstuian.app.ui.blooddonationrequestcreate

import com.workfort.pstuian.view.service.StateReducer


class BloodDonationRequestCreateScreenStateReducer : StateReducer<BloodDonationRequestCreateScreenState, BloodDonationRequestCreateScreenStateUpdate> {
 override val initial: BloodDonationRequestCreateScreenState
  get() = BloodDonationRequestCreateScreenState()
}