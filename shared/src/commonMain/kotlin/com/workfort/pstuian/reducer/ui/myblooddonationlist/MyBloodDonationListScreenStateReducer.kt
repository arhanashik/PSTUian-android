package com.workfort.pstuian.reducer.ui.myblooddonationlist

import com.workfort.pstuian.reducer.service.StateReducer


class MyBloodDonationListScreenStateReducer : StateReducer<MyBloodDonationListScreenState, MyBloodDonationListScreenStateUpdate> {
    override val initial: MyBloodDonationListScreenState
        get() = MyBloodDonationListScreenState()
}