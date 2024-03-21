package com.workfort.pstuian.app.ui.students.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.faculty.listener.StudentClickEvent
import com.workfort.pstuian.app.ui.students.viewholder.StudentsViewHolder
import com.workfort.pstuian.databinding.RowStudentBinding
import java.util.Locale

class StudentsAdapter : RecyclerView.Adapter<StudentsViewHolder>(), Filterable {

    private val data : MutableList<StudentEntity> = ArrayList()
    private val filteredData : MutableList<StudentEntity> = ArrayList()
    private var listener: StudentClickEvent? = null

    fun setData(data: MutableList<StudentEntity>) {
        this.data.clear()
        this.data.addAll(data)
        filterData(data)
    }

    fun addData(data: MutableList<StudentEntity>) {
        if(data.isEmpty()) return

        val newData = if(itemCount == 0) data else data.toSet().minus(this.data.toSet())
        if(newData.isEmpty()) return

        val startPosition = itemCount
        this.data.addAll(newData)
        this.filteredData.addAll(newData)

        val newDataCount = newData.size
        if(newDataCount == 1) notifyItemInserted(startPosition)
        else notifyItemRangeInserted(startPosition, newDataCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<StudentEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun setListener(listener: StudentClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowStudentBinding.inflate(inflater, parent, false)
        return StudentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        val student = filteredData[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickStudent(student)
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<StudentEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.id.toString().contains(q)
                            || it.reg.lowercase(Locale.ROOT).contains(q)
                            || it.name.lowercase(Locale.ROOT).contains(q)
                            || (it.blood?: "").lowercase(Locale.ROOT).contains(q)
                            || (it.phone?: "").lowercase(Locale.ROOT).contains(q)
                            || (it.email?: "").lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<StudentEntity>
                filterData(newData)
            }
        }
    }
}