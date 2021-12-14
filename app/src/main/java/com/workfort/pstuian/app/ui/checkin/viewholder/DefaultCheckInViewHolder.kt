package com.workfort.pstuian.app.ui.checkin.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.databinding.RowCheckInUserBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 13:00.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class DefaultCheckInViewHolder (val binding: RowCheckInUserBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(onClickCheckIn : () -> Unit) {
        with(binding) {
            root.setOnClickListener { onClickCheckIn() }
        }
    }
}