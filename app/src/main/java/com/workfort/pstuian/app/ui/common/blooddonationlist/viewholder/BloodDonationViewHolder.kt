package com.workfort.pstuian.app.ui.common.blooddonationlist.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.RowBloodDonationBinding
import com.workfort.pstuian.app.ui.common.dialog.CommonDialog

/**
 *  ****************************************************************************
 *  * Created by : arhan on 16 Dec, 2021 at 3:20.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class BloodDonationViewHolder(val binding: RowBloodDonationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        showAction: Boolean,
        item: BloodDonationEntity,
        onClickEdit : (item: BloodDonationEntity) -> Unit,
        onClickDelete : (item: BloodDonationEntity) -> Unit,
    ) {
        with(binding) {
            val requestId = if(item.requestId == null || item.requestId == 0) "Unregistered"
            else item.requestId.toString()
            val title = "Request Id : $requestId"
            tvTitle.text = title
            tvInfo.text = item.info
            val date = item.date.split(" ")[0]
            tvDate.text = date
            cgActions.visibility = if(showAction) View.VISIBLE else View.GONE
            chipEdit.setOnClickListener { onClickEdit(item) }
            chipDelete.setOnClickListener { onClickDelete(item) }
            root.setOnClickListener {
                showDetails(root.context, title, item.info?: "")
            }
        }
    }

    private fun showDetails(context: Context, title: String, message: String) {
        CommonDialog.success(
            context,
            title,
            message,
            icon = R.drawable.ic_blood_drop
        )
    }
}