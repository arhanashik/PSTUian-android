package com.workfort.pstuian.app.ui.common.locationpicker.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.checkinlocation.CheckInLocationEntity
import com.workfort.pstuian.databinding.RowLocationBinding
import com.workfort.pstuian.util.helper.MathUtil
import java.text.DecimalFormatSymbols

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 22:48.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInLocationViewHolder(private val binding : RowLocationBinding) :
    RecyclerView.ViewHolder(binding.root)  {
    fun bind(item: CheckInLocationEntity, onSelect : (location: CheckInLocationEntity) -> Unit) {
        with(binding) {
            tvName.text = item.name
            val symbols = DecimalFormatSymbols.getInstance()
            symbols.groupingSeparator = ' '

            val formattedCount = MathUtil.prettyCount(item.count)
            val checkInCountStr = "$formattedCount check in"
            tvCheckInCount.text = checkInCountStr
            ivImage.load(item.imageUrl?: "") {
                placeholder(R.drawable.ic_location_placeholder)
                error(R.drawable.ic_location_placeholder)
            }

            root.setOnClickListener { onSelect(item) }
        }
    }
}