package com.workfort.pstuian.app.ui.faculty.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.faculty.listener.CourseScheduleClickEvent
import com.workfort.pstuian.app.ui.faculty.viewholder.CourseViewHolder
import com.workfort.pstuian.databinding.RowCourseBinding
import java.util.Locale

class CourseAdapter :
    RecyclerView.Adapter<CourseViewHolder>(), Filterable {

    private val data : MutableList<CourseEntity> = ArrayList()
    private val filteredData : MutableList<CourseEntity> = ArrayList()
    private var listener: CourseScheduleClickEvent? = null

    fun setData(data: MutableList<CourseEntity>) {
        this.data.clear()
        this.data.addAll(data)
        filterData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<CourseEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun setListener(listener: CourseScheduleClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowCourseBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val student = filteredData[position]

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                listener?.onClickCourseSchedule(student)
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<CourseEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.courseCode.lowercase(Locale.ROOT).contains(q)
                            || it.courseTitle.lowercase(Locale.ROOT).contains(q)
                            || it.creditHour.lowercase(Locale.ROOT).contains(q)
                            || it.status.toString().lowercase(Locale.ROOT).contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                val newData = filteredResult?.values as ArrayList<CourseEntity>
                filterData(newData)
            }
        }
    }
}