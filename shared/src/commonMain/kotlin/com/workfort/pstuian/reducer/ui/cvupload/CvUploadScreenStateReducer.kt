package com.workfort.pstuian.reducer.ui.cvupload

import com.workfort.pstuian.reducer.service.StateReducer

class CvUploadScreenStateReducer : StateReducer<CvUploadScreenState, CvUploadScreenStateUpdate> {
    override val initial: CvUploadScreenState
        get() = CvUploadScreenState()
}
