package com.workfort.pstuian.app.ui.home.faculty.holder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.databinding.RowStudentBinding
import com.workfort.pstuian.util.helper.ImageLoader

class StudentsViewHolder (val binding: RowStudentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(student: StudentEntity) {
        ImageLoader.load(binding.imgAvatar, student.imageUrl)

        binding.tvName.text = student.name
        val details = student.id + "    :    " + student.reg
        binding.tvDesignationStatus.text = details
    }
}