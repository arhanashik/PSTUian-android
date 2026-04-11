package com.workfort.pstuian.app.ui.imageupload

import com.workfort.pstuian.reducer.service.StateReducer


class ImageUploadScreenStateReducer : StateReducer<ImageUploadScreenState, ImageUploadScreenStateUpdate> {
 override val initial: ImageUploadScreenState
  get() = ImageUploadScreenState()
}