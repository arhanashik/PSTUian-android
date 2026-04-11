package com.workfort.pstuian.reducer.ui.blooddonationcreate

import com.workfort.pstuian.reducer.service.StateReducer


class BloodDonationCreateScreenStateReducer : StateReducer<BloodDonationCreateScreenState, BloodDonationCreateScreenStateUpdate> {
 override val initial: BloodDonationCreateScreenState
  get() = BloodDonationCreateScreenState()
}