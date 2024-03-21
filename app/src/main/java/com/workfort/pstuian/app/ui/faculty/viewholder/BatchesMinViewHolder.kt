package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.databinding.RowBatchMinBinding

class BatchesMinViewHolder (val binding: RowBatchMinBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(batch: BatchEntity) {
        with(binding) {
            tvName.text = batch.name
            tvSession.text = batch.session
        }
    }
}