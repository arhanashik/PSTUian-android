package com.workfort.pstuian.app.ui.home.faculty.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.databinding.RowEmployeeBinding
import com.workfort.pstuian.util.helper.ImageLoader

class EmployeeViewHolder (val binding: RowEmployeeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(employee: EmployeeEntity) {
        ImageLoader.load(binding.imgAvatar, employee.imageUrl)

        binding.tvName.text = employee.name
        var details = employee.designation
        if(!TextUtils.isEmpty(employee.department))
            details += "    :    " + employee.department
        binding.tvDesignationDepartment.text = details
    }
}