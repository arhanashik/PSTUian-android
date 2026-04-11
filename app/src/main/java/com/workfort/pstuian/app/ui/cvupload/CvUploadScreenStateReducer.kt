package com.workfort.pstuian.app.ui.cvupload

import com.workfort.pstuian.reducer.service.StateReducer


class CvUploadScreenStateReducer : StateReducer<CvUploadScreenState, CvUploadScreenStateUpdate> {
 override val initial: CvUploadScreenState
  get() = CvUploadScreenState()
}