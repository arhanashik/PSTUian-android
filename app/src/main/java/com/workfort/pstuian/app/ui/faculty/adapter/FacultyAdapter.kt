package com.workfort.pstuian.app.ui.faculty.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.faculty.viewholder.FacultyViewHolder
import com.workfort.pstuian.databinding.RowFacultyBinding

class FacultyAdapter(
    private val onClickItem : (faculty: FacultyEntity) -> Unit
) : RecyclerView.Adapter<FacultyViewHolder>() {
    private val data : MutableList<FacultyEntity> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(faculties: MutableList<FacultyEntity>) {
        this.data.clear()
        this.data.addAll(faculties)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItems() = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowFacultyBinding.inflate(inflater, parent, false)
        return FacultyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        val faculty = data[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(faculty)
        holder.binding.root.setOnClickListener { onClickItem(faculty) }
    }
}