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
            val title = item.title.ifEmpty { "~" }
            tvInfo.text = title
            if(item.actionIcon == ProfileInfoItem.NO_ACTION_ICON) {
                btnAction.visibility = View.INVISIBLE
                binding.root.setOnClickListener(null)
            } else {
                btnAction.visibility = View.VISIBLE
                btnAction.setImageResource(item.actionIcon)
                binding.root.setOnClickListener { callback?.onAction(item) }
            }
        }
    }
}