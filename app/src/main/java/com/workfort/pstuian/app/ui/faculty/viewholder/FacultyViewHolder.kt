package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.databinding.RowFacultyBinding

class FacultyViewHolder (val binding: RowFacultyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(faculty: FacultyEntity, @DrawableRes bgResource: Int) {
        with(binding) {
            tvTitle.text = faculty.shortTitle
//            container.setBackgroundResource(bgResource)
        }
    }
}