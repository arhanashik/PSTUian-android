package com.workfort.pstuian.app.ui.cvdownload

import com.workfort.pstuian.view.service.StateReducer


class CvDownloadScreenStateReducer : StateReducer<CvDownloadScreenState, CvDownloadScreenStateUpdate> {
 override val initial: CvDownloadScreenState
  get() = CvDownloadScreenState()
}