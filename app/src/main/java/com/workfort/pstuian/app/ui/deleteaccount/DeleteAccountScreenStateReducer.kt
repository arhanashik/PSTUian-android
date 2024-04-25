package com.workfort.pstuian.app.ui.deleteaccount

import com.workfort.pstuian.view.service.StateReducer


class DeleteAccountScreenStateReducer : StateReducer<DeleteAccountScreenState, DeleteAccountScreenStateUpdate> {
 override val initial: DeleteAccountScreenState
  get() = DeleteAccountScreenState()
}