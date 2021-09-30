package com.workfort.pstuian.app.ui.home.faculty.holder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.databinding.RowTeacherBinding
import com.workfort.pstuian.util.helper.ImageLoader

class TeachersViewHolder (val binding: RowTeacherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(teacher: TeacherEntity) {
        ImageLoader.load(binding.imgAvatar, teacher.imageUrl)

        binding.tvName.text = teacher.name
        val details = teacher.designation + "  :  " + teacher.status
        binding.tvDesignationStatus.text = details
    }
}