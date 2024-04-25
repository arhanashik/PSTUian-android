package com.workfort.pstuian.app.ui.myblooddonationlist

import com.workfort.pstuian.view.service.StateReducer


class MyBloodDonationListScreenStateReducer : StateReducer<MyBloodDonationListScreenState, MyBloodDonationListScreenStateUpdate> {
 override val initial: MyBloodDonationListScreenState
  get() = MyBloodDonationListScreenState()
}