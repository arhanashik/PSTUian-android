package com.workfort.pstuian.app.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.ui.common.viewholder.ProfileInfoViewHolder
import com.workfort.pstuian.databinding.RowProfileInfoBinding

class ProfileInfoAdapter : RecyclerView.Adapter<ProfileInfoViewHolder>() {

    private val items : MutableList<ProfileInfoItem> = ArrayList()
    private var listener: ProfileInfoClickEvent? = null

    fun setItems(items: MutableList<ProfileInfoItem>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun setListener(listener: ProfileInfoClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ProfileInfoViewHolder(RowProfileInfoBinding.inflate(
            inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProfileInfoViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }
}

data class ProfileInfoItem (
    val label: String,
    val title: String,
    @DrawableRes
    var actionIcon: Int = -1,
    var action: ProfileInfoAction = ProfileInfoAction.NONE,
    var actionData: String? = ""
)

abstract class ProfileInfoClickEvent {
    open fun onAction(item: ProfileInfoItem) = Unit
}

sealed class ProfileInfoAction {
    object NONE : ProfileInfoAction()
    object EDIT : ProfileInfoAction()
    object CALL : ProfileInfoAction()
    object MAIL : ProfileInfoAction()
    object DOWNLOAD : ProfileInfoAction()
    object LINK : ProfileInfoAction()
    object PASSWORD : ProfileInfoAction()
}