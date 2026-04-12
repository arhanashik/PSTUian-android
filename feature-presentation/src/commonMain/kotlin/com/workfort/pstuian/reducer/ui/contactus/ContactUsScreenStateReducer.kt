package com.workfort.pstuian.reducer.ui.contactus

import com.workfort.pstuian.reducer.service.StateReducer


class ContactUsScreenStateReducer : StateReducer<ContactUsScreenState, ContactUsScreenStateUpdate> {
 override val initial: ContactUsScreenState
  get() = ContactUsScreenState()
}