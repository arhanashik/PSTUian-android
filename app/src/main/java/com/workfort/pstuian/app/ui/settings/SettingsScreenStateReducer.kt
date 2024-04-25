package com.workfort.pstuian.app.ui.settings

import com.workfort.pstuian.view.service.StateReducer


class SettingsScreenStateReducer : StateReducer<SettingsScreenState, SettingsScreenStateUpdate> {
 override val initial: SettingsScreenState
  get() = SettingsScreenState()
}