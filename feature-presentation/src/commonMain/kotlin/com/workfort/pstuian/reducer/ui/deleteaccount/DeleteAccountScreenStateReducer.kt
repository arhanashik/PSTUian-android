package com.workfort.pstuian.reducer.ui.deleteaccount

import com.workfort.pstuian.reducer.service.StateReducer


class DeleteAccountScreenStateReducer : StateReducer<DeleteAccountScreenState, DeleteAccountScreenStateUpdate> {
 override val initial: DeleteAccountScreenState
  get() = DeleteAccountScreenState()
}