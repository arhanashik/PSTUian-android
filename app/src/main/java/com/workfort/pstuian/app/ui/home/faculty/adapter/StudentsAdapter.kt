package com.workfort.pstuian.app.ui.home.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.databinding.RowStudentBinding
import com.workfort.pstuian.app.ui.home.faculty.holder.StudentsViewHolder
import com.workfort.pstuian.app.ui.home.faculty.listener.StudentClickEvent

class StudentsAdapter : RecyclerView.Adapter<StudentsViewHolder>(), Filterable {

    private val students : MutableList<StudentEntity> = ArrayList()
    private val filteredStudents : MutableList<StudentEntity> = ArrayList()
    private var listener: StudentClickEvent? = null

    fun setStudents(students: MutableList<StudentEntity>) {
        this.students.clear()
        this.students.addAll(students)

        filter.filter("")
    }

    fun setListener(listener: StudentClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<StudentEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(students)
                }else {
                    val q = query.toString().toLowerCase()
                    students.forEach {
                        if(it.id!!.toLowerCase().contains(q)
                            || it.reg!!.toLowerCase().contains(q)
                            || it.name!!.toLowerCase().contains(q)
                            || it.blood!!.toLowerCase().contains(q)
                            || it.phone!!.toLowerCase().contains(q)
                            || it.email!!.toLowerCase().contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredStudents.clear()
                filteredStudents.addAll(filteredResult?.values as ArrayList<StudentEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredStudents.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.row_student, parent, false)
                as RowStudentBinding
        return StudentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        val student = filteredStudents[position]

        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickStudent(student)
            }
        }
    }


}