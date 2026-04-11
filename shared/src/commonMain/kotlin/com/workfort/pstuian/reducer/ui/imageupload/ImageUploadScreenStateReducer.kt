package com.workfort.pstuian.reducer.ui.imageupload

import com.workfort.pstuian.reducer.service.StateReducer

class ImageUploadScreenStateReducer : StateReducer<ImageUploadScreenState, ImageUploadScreenStateUpdate> {
    override val initial: ImageUploadScreenState
        get() = ImageUploadScreenState()
}
