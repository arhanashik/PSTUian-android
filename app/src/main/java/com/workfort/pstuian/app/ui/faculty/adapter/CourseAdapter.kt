package com.workfort.pstuian.app.ui.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.ui.faculty.viewholder.CourseViewHolder
import com.workfort.pstuian.app.ui.faculty.listener.CourseScheduleClickEvent
import com.workfort.pstuian.databinding.RowCourseBinding
import java.util.*
import kotlin.collections.ArrayList

class CourseAdapter :
    RecyclerView.Adapter<CourseViewHolder>(), Filterable {

    private val courses : MutableList<CourseEntity> = ArrayList()
    private val filteredCourses : MutableList<CourseEntity> = ArrayList()
    private var listener: CourseScheduleClickEvent? = null

    fun setCourseSchedules(courses: MutableList<CourseEntity>) {
        this.courses.clear()
        this.courses.addAll(courses)

        filter.filter("")
    }

    fun setListener(listener: CourseScheduleClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<CourseEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(courses)
                } else {
                    val q = query.toString().toLowerCase(Locale.ROOT)
                    courses.forEach {
                        if(it.courseCode.toLowerCase(Locale.ROOT).contains(q)
                            || it.courseTitle.toLowerCase(Locale.ROOT).contains(q)
                            || it.creditHour.toLowerCase(Locale.ROOT).contains(q)
                            || it.status.toString().toLowerCase(Locale.ROOT).contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredCourses.clear()
                @Suppress("UNCHECKED_CAST")
                filteredCourses.addAll(filteredResult?.values as ArrayList<CourseEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredCourses.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowCourseBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val student = filteredCourses[position]

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                listener?.onClickCourseSchedule(student)
            }
        }
    }
}