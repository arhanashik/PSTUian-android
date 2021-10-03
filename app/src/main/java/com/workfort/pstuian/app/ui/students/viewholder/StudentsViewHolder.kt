package com.workfort.pstuian.app.ui.students.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.databinding.RowStudentBinding

class StudentsViewHolder (val binding: RowStudentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(student: StudentEntity) {
        with(binding) {
            imgAvatar.load(student.imageUrl)
            tvName.text = student.name
            tvId.text = student.id.toString()
            tvReg.text = student.reg
            tvBloodGroup.text = student.blood
        }
    }
}