package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.databinding.RowFacultyBinding

class FacultyViewHolder (val binding: RowFacultyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(faculty: FacultyEntity) {
        with(binding) {
            tvTitle.text = faculty.shortTitle
            ivIcon.load(faculty.icon) {
                placeholder(R.drawable.ic_education_gray)
                error(R.drawable.ic_education_gray)
            }
        }
    }
}