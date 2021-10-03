package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.databinding.RowTeacherBinding

class TeachersViewHolder (val binding: RowTeacherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(teacher: TeacherEntity) {
        with(binding) {
            imgAvatar.load(teacher.imageUrl)
            tvName.text = teacher.name
            tvDesignation.text = teacher.designation
            tvDepartment.text = teacher.department
        }
    }
}