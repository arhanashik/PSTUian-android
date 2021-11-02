package com.workfort.pstuian.app.ui.faculty.viewholder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.databinding.RowBatchBinding

class BatchesViewHolder (val binding: RowBatchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(batch: BatchEntity) {
        with(binding) {
            tvName.text = batch.name
            tvTitle.text = batch.title
            tvSession.text = batch.session
            tvStudentCount.text = batch.totalStudent.toString()

            tvTitle.visibility = if (TextUtils.isEmpty(batch.title))
                View.GONE else View.VISIBLE
        }
    }
}