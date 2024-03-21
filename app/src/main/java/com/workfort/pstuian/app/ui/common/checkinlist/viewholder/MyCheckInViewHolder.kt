package com.workfort.pstuian.app.ui.common.checkinlist.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.RowMyCheckInBinding
import com.workfort.pstuian.util.helper.DateUtil
import com.workfort.pstuian.util.helper.MathUtil

/**
 *  ****************************************************************************
 *  * Created by : arhan on 15 Dec, 2021 at 22:38.
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

class MyCheckInViewHolder (private val binding : RowMyCheckInBinding) :
    RecyclerView.ViewHolder(binding.root)  {
    fun bind(item: CheckInEntity, onClick : (item: CheckInEntity) -> Unit) {
        with(binding) {
            tvName.text = item.locationName
            tvDate.text = DateUtil.getTimeAgo(item.date)
            val formattedCount = MathUtil.prettyCount(item.count)
            val checkInCountStr = "$formattedCount check in"
            tvCheckInCount.text = checkInCountStr
            tvPrivacy.text = when(item.privacy) {
                NetworkConst.Params.CheckInPrivacy.ONLY_ME -> "Only Me"
                else -> "Public"
            }

            ivImage.load(item.locationImageUrl?: "") {
                placeholder(R.drawable.ic_location_placeholder)
                error(R.drawable.ic_location_placeholder)
            }

            root.setOnClickListener { onClick(item) }
        }
    }
}