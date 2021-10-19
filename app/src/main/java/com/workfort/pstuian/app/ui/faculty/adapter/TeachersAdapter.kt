package com.workfort.pstuian.app.ui.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.databinding.RowTeacherBinding
import com.workfort.pstuian.app.ui.faculty.viewholder.TeachersViewHolder
import com.workfort.pstuian.app.ui.faculty.listener.TeacherClickEvent
import java.util.*
import kotlin.collections.ArrayList

class TeachersAdapter : RecyclerView.Adapter<TeachersViewHolder>(), Filterable {

    private val teachers : MutableList<TeacherEntity> = ArrayList()
    private val filteredTeachers : MutableList<TeacherEntity> = ArrayList()
    private var listener: TeacherClickEvent? = null

    fun setTeachers(teachers: MutableList<TeacherEntity>) {
        this.teachers.clear()
        this.teachers.addAll(teachers)

        filter.filter("")
    }

    fun setListener(listener: TeacherClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<TeacherEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(teachers)
                } else {
                    val q = query.toString().toLowerCase(Locale.ROOT)
                    teachers.forEach {
                        if(it.name.toLowerCase(Locale.ROOT).contains(q)
                            || it.department.toLowerCase(Locale.ROOT).contains(q)
                            || it.designation.toLowerCase(Locale.ROOT).contains(q)
                            || (it.email?: "").toLowerCase(Locale.ROOT).contains(q)
                            || (it.status?: "").toLowerCase(Locale.ROOT).contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredTeachers.clear()
                @Suppress("UNCHECKED_CAST")
                filteredTeachers.addAll(filteredResult?.values as ArrayList<TeacherEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredTeachers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeachersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.row_teacher, parent, false)
                as RowTeacherBinding
        return TeachersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeachersViewHolder, position: Int) {
        val teacher = filteredTeachers[position]

        holder.bind(teacher)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickTeacher(teacher)
            }
        }
    }
}