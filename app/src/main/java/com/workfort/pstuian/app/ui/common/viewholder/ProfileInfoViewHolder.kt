package com.workfort.pstuian.app.ui.common.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoClickEvent
import com.workfort.pstuian.app.ui.common.adapter.ProfileInfoItem
import com.workfort.pstuian.databinding.RowProfileInfoBinding

class ProfileInfoViewHolder (val binding: RowProfileInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileInfoItem, callback: ProfileInfoClickEvent?) {
        with(binding) {
            labelInfo.text = item.label
            tvInfo.text = item.title
            if(item.actionIcon == -1) {
                btnAction.visibility = View.INVISIBLE
                btnAction.setOnClickListener {  }
            } else {
                btnAction.visibility = View.VISIBLE
                btnAction.setImageResource(item.actionIcon)
                btnAction.setOnClickListener { callback?.onAction(item) }
            }
        }
    }
}