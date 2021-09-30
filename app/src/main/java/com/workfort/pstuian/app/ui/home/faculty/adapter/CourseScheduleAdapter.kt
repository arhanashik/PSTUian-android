package com.workfort.pstuian.app.ui.home.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.courseschedule.CourseScheduleEntity
import com.workfort.pstuian.databinding.RowCourseScheduleBinding
import com.workfort.pstuian.app.ui.home.faculty.holder.CourseScheduleViewHolder
import com.workfort.pstuian.app.ui.home.faculty.listener.CourseScheduleClickEvent

class CourseScheduleAdapter :
    RecyclerView.Adapter<CourseScheduleViewHolder>(), Filterable {

    private val courseSchedules : MutableList<CourseScheduleEntity> = ArrayList()
    private val filteredCourseSchedules : MutableList<CourseScheduleEntity> = ArrayList()
    private var listener: CourseScheduleClickEvent? = null

    fun setCourseSchedules(courseSchedules: MutableList<CourseScheduleEntity>) {
        this.courseSchedules.clear()
        this.courseSchedules.addAll(courseSchedules)

        filter.filter("")
    }

    fun setListener(listener: CourseScheduleClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<CourseScheduleEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(courseSchedules)
                }else {
                    val q = query.toString().toLowerCase()
                    courseSchedules.forEach {
                        if(it.courseCode!!.toLowerCase().contains(q)
                            || it.courseTitle!!.toLowerCase().contains(q)
                            || it.creditHour!!.toLowerCase().contains(q)
                            || it.status.toString().toLowerCase().contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredCourseSchedules.clear()
                filteredCourseSchedules.addAll(filteredResult?.values as ArrayList<CourseScheduleEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredCourseSchedules.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.row_course_schedule, parent, false)
                as RowCourseScheduleBinding
        return CourseScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseScheduleViewHolder, position: Int) {
        val student = filteredCourseSchedules[position]

        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickCourseSchedule(student)
            }
        }
    }
}