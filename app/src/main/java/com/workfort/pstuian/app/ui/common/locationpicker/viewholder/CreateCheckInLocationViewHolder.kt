package com.workfort.pstuian.app.ui.common.locationpicker.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.databinding.RowCreateLocationBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 15 Dec, 2021 at 1:47.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class CreateCheckInLocationViewHolder(private val binding : RowCreateLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(onCreateNew : () -> Unit) {
        binding.root.setOnClickListener { onCreateNew() }
    }
}