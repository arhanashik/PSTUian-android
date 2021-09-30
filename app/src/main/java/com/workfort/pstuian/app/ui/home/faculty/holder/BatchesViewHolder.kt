package com.workfort.pstuian.app.ui.home.faculty.holder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.databinding.RowBatchBinding

class BatchesViewHolder (val binding: RowBatchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(batch: BatchEntity) {
        val name = batch.faculty + " " + batch.name + "  |  " + batch.title
        binding.tvName.text = name
        val details = batch.session + "    :    " + batch.totalStudent + " students"
        binding.tvTitle.text = details

        binding.tvTitle.visibility = if (TextUtils.isEmpty(batch.title))
            View.GONE else View.VISIBLE
    }
}