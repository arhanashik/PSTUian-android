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
    private val data : MutableList<NotificationEntity> = ArrayList()
    private var listener: ItemClickEvent<NotificationEntity>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(notifications: MutableList<NotificationEntity>) {
        this.data.clear()
        this.data.addAll(notifications)
        notifyDataSetChanged()
    }

    fun addData(data: MutableList<NotificationEntity>) {
        if(data.isEmpty()) return

        val newData = if(itemCount == 0) data else data.toSet().minus(this.data.toSet())
        if(newData.isEmpty()) return

        val startPosition = itemCount
        this.data.addAll(newData)

        val newDataCount = newData.size
        if(newDataCount == 1) notifyItemInserted(startPosition)
        else notifyItemRangeInserted(startPosition, newDataCount)
    }

    fun setListener(listener: ItemClickEvent<NotificationEntity>) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun clear() {
        val lastPosition = itemCount
        this.data.clear()
        notifyItemRangeRemoved(0, lastPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowNotificationBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = data[position]

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