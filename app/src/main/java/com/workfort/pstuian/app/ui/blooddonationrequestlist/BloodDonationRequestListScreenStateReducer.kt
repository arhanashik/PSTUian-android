package com.workfort.pstuian.app.ui.blooddonationrequestlist

import com.workfort.pstuian.view.service.StateReducer


class BloodDonationRequestListScreenStateReducer : StateReducer<BloodDonationRequestListScreenState, BloodDonationRequestListScreenStateUpdate> {
 override val initial: BloodDonationRequestListScreenState
  get() = BloodDonationRequestListScreenState()
}