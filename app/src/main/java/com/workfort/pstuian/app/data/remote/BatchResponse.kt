package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.batch.BatchEntity

data class BatchResponse (val success: Boolean,
                          val message: String,
                          val batches: ArrayList<BatchEntity>)