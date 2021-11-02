package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.app.data.local.batch.BatchEntity

interface BatchClickEvent{
    fun onClickBatch(batch: BatchEntity)
}