package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.databinding.RowEmployeeBinding

class EmployeeViewHolder (val binding: RowEmployeeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(employee: EmployeeEntity) {
        with(binding) {
            imgAvatar.load(employee.imageUrl)
            tvName.text = employee.name
            tvDesignation.text = employee.designation
            tvDepartment.text = employee.department
        }
    }
}