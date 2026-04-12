package com.workfort.pstuian.reducer.ui.cvdownload

import com.workfort.pstuian.reducer.service.StateReducer

class CvDownloadScreenStateReducer : StateReducer<CvDownloadScreenState, CvDownloadScreenStateUpdate> {
    override val initial: CvDownloadScreenState
        get() = CvDownloadScreenState()
}
