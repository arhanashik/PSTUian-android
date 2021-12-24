package com.workfort.pstuian.app.ui.checkin.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.databinding.RowCheckInUserBinding
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.Toaster

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

class CheckInViewHolder (val binding: RowCheckInUserBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CheckInEntity, onClickItem : (item: CheckInEntity) -> Unit) {
        with(binding) {
            ivAvatar.load(item.imageUrl?: "") {
                placeholder(R.drawable.img_placeholder_profile)
                error(R.drawable.img_placeholder_profile)
            }
            tvName.text = item.name
            tvBatch.text = item.batch
            btnCall.setOnClickListener { promptCall(root.context, item.phone.trim()) }

            root.setOnClickListener { onClickItem(item) }
        }
    }

    private fun promptCall(context: Context, phoneNumber: String) {
        if(phoneNumber.isEmpty()) {
            Toaster.show(R.string.txt_error_call)
            return
        }
        val msg = "${context.getString(R.string.txt_msg_call)} $phoneNumber"
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.txt_title_call)
            .setMessage(msg)
            .setPositiveButton(R.string.txt_call) { _, _ ->
                LinkUtil(context).callTo(phoneNumber)
            }
            .setNegativeButton(R.string.txt_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}