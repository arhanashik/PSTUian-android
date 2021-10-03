package com.workfort.pstuian.app.ui.faculty.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.databinding.RowCourseBinding

class CourseViewHolder (val binding: RowCourseBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(course: CourseEntity) {
        with(binding) {
            tvCourseCode.text = course.courseCode
            tvCourseTitle.text = course.courseTitle
            tvCreditHour.text = course.creditHour
        }
    }
}