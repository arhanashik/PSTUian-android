package com.workfort.pstuian.app.ui.contactus

import com.workfort.pstuian.view.service.StateReducer


class ContactUsScreenStateReducer : StateReducer<ContactUsScreenState, ContactUsScreenStateUpdate> {
 override val initial: ContactUsScreenState
  get() = ContactUsScreenState()
}