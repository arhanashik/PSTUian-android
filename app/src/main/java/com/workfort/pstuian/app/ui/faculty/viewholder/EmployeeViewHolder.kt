package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.databinding.RowEmployeeBinding

class EmployeeViewHolder (val binding: RowEmployeeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(employee: EmployeeEntity) {
        with(binding) {
            if(employee.imageUrl.isNullOrEmpty()) {
                imgAvatar.setImageResource(R.drawable.img_placeholder_profile)
            } else {
                imgAvatar.load(employee.imageUrl) {
                    placeholder(R.drawable.img_placeholder_profile)
                    error(R.drawable.img_placeholder_profile)
                }
            }
            tvName.text = employee.name
            tvDesignation.text = employee.designation
            tvDepartment.text = employee.department
        }
    }
}