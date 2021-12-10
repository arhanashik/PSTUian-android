package com.workfort.pstuian.app.ui.notification.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.notification.NotificationEntity
import com.workfort.pstuian.app.ui.base.callback.ItemClickEvent
import com.workfort.pstuian.app.ui.notification.viewholder.NotificationViewHolder
import com.workfort.pstuian.databinding.RowNotificationBinding

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:17.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class NotificationAdapter : RecyclerView.Adapter<NotificationViewHolder>() {
    private val notifications : MutableList<NotificationEntity> = ArrayList()
    private var listener: ItemClickEvent<NotificationEntity>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(notifications: MutableList<NotificationEntity>) {
        this.notifications.clear()
        this.notifications.addAll(notifications)
        notifyDataSetChanged()
    }

    fun setListener(listener: ItemClickEvent<NotificationEntity>) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowNotificationBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = notifications[position]

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(item)
        holder.binding.root.setOnClickListener {
            run {
                listener?.onClickItem(item)
            }
        }
    }
}