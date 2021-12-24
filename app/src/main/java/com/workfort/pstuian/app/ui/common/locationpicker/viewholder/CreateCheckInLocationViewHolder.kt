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
 *  *
 *  * Last edited by : arhan on 2021/12/15.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CreateCheckInLocationViewHolder(private val binding : RowCreateLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(onCreateNew : () -> Unit) {
        binding.root.setOnClickListener { onCreateNew() }
    }
}