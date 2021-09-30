package com.workfort.pstuian.app.ui.home.faculty.holder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.databinding.RowFacultyBinding

class FacultyViewHolder (val binding: RowFacultyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(faculty: FacultyEntity) {
        //val title = (adapterPosition + 1).toString() + ". " + faculty.title
        binding.tvTitle.text = faculty.title
    }
}