package com.workfort.pstuian.app.ui.blooddonationrequest.viewholder

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.blooddonationrequest.BloodDonationRequestEntity
import com.workfort.pstuian.databinding.ItemChipBinding
import com.workfort.pstuian.databinding.RowBloodDonationRequestBinding
import com.workfort.pstuian.util.helper.LinkUtil
import com.workfort.pstuian.util.helper.Toaster
import com.workfort.pstuian.util.view.dialog.CommonDialog

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 20:49.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/10.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BloodDonationRequestViewHolder (val binding: RowBloodDonationRequestBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: BloodDonationRequestEntity) {
        with(binding) {
            ivAvatar.load(entity.imageUrl) {
                placeholder(R.drawable.img_placeholder_profile)
                error(R.drawable.img_placeholder_profile)
            }
            tvName.text = entity.name
            val date = entity.beforeDate.split(" ")[0]
            val title = "Need ${entity.bloodGroup} blood before $date"
            tvTitle.text = title
            tvInfo.text = entity.info
            cgActions.removeAllViews()
            entity.contacts.split(",").forEach { contact ->
                val inflater = LayoutInflater.from(cgActions.context)
                val chip = ItemChipBinding.inflate(inflater, cgActions,
                    false).root.apply {
                    text = contact.trim()
                    setOnClickListener { promptCall(root.context, contact.trim()) }
                }
                cgActions.addView(chip)
            }
            root.setOnClickListener {
                showDetails(root.context, title, entity.info?: "")
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

    private fun promptCall(context: Context, phoneNumber: String) {
        if(phoneNumber.isEmpty()) {
            Toaster.show(R.string.txt_error_call)
            return
        }
        val msg = "${context.getString(R.string.txt_msg_call)} $phoneNumber"
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.txt_title_call)
            .setMessage(msg)
            .setPositiveButton(R.string.txt_call) { _,_ ->
                LinkUtil(context).callTo(phoneNumber)
            }
            .setNegativeButton(R.string.txt_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}