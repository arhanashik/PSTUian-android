package com.workfort.pstuian.app.ui.notification.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.RowNotificationBinding
import com.workfort.pstuian.util.helper.DateUtil

class NotificationViewHolder (val binding: RowNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationEntity) {
        with(binding) {
            val iconRes = when(item.type) {
                Const.NotificationType.DEFAULT -> R.drawable.ic_bell_filled
                Const.NotificationType.BLOOD_DONATION -> R.drawable.ic_blood_drop
                Const.NotificationType.NEWS -> R.drawable.ic_newspaper
                Const.NotificationType.HELP -> R.drawable.ic_hand_heart
                else -> R.drawable.ic_bell_filled
            }
            icType.setImageResource(iconRes)
            tvTitle.text = item.title?: "Notification"
            tvMessage.text = item.message
            tvDate.text = DateUtil.getTimeAgo(item.date?: "")
        }
    }
}