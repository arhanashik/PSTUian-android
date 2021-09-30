package com.workfort.pstuian.app.ui.home.faculty.holder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleEntity
import com.workfort.pstuian.databinding.RowCourseScheduleBinding

class CourseScheduleViewHolder (val binding: RowCourseScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(courseSchedule: CourseScheduleEntity) {
        binding.tvCourseCode.text = courseSchedule.courseCode
        binding.tvCourseTitle.text = courseSchedule.courseTitle
        val creditHour = courseSchedule.creditHour + " Cr"
        binding.tvCreditHour.text = creditHour
    }
}