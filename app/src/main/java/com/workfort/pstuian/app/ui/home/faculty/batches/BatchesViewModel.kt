package com.workfort.pstuian.app.ui.home.faculty.batches

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.batch.BatchService
import com.workfort.pstuian.app.data.local.database.DatabaseHelper

class BatchesViewModel : ViewModel() {
    private var batchService: BatchService = DatabaseHelper.provideBatchService()
    private lateinit var batchesLiveData: LiveData<List<BatchEntity>>

    fun getBatches(faculty: String): LiveData<List<BatchEntity>> {
        batchesLiveData = batchService.getAllLive(faculty)

        return batchesLiveData
    }

    fun insertBatches(batches: ArrayList<BatchEntity>) {
        Thread { batchService.insertAll(batches) }.start()
    }
}
