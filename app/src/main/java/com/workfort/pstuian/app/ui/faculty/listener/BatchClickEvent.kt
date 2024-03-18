package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.model.BatchEntity

interface BatchClickEvent{
    fun onClickBatch(batch: BatchEntity)
}