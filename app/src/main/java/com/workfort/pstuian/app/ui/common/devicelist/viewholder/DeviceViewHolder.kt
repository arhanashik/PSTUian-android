package com.workfort.pstuian.app.ui.common.devicelist.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.databinding.RowDeviceBinding
import com.workfort.pstuian.util.helper.DateUtil

/**
 *  ****************************************************************************
 *  * Created by : arhan on 23 Dec, 2021 at 22:32.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class DeviceViewHolder (private val binding : RowDeviceBinding) :
    RecyclerView.ViewHolder(binding.root)  {
    fun bind(item: DeviceEntity, onClick : (item: DeviceEntity) -> Unit) {
        with(binding) {
            tvName.text = item.model?: "Unknown"
            val ipAddress = "IP Address: ${item.ipAddress}"
            tvIpAddress.text = ipAddress
            val lastActiveAt = "Last Use: " + DateUtil.getTimeAgo(item.updatedAt?: "")
            tvLastActive.text = lastActiveAt
            root.setOnClickListener { onClick(item) }
        }
    }
}