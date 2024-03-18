package com.workfort.pstuian.app.ui.faculty.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.faculty.listener.TeacherClickEvent
import com.workfort.pstuian.app.ui.faculty.viewholder.TeachersViewHolder
import com.workfort.pstuian.databinding.RowTeacherBinding
import java.util.Locale

class TeachersAdapter : RecyclerView.Adapter<TeachersViewHolder>(), Filterable {

    private val data : MutableList<TeacherEntity> = ArrayList()
    private val filteredData : MutableList<TeacherEntity> = ArrayList()
    private var listener: TeacherClickEvent? = null

    fun setData(teachers: MutableList<TeacherEntity>) {
        this.data.clear()
        this.data.addAll(teachers)
        filterData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<TeacherEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun setListener(listener: TeacherClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeachersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTeacherBinding.inflate(inflater, parent, false)
        return TeachersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeachersViewHolder, position: Int) {
        val teacher = filteredData[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(teacher)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickTeacher(teacher)
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<TeacherEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.name.lowercase(Locale.ROOT).contains(q)
                            || it.department.lowercase(Locale.ROOT).contains(q)
                            || it.designation.lowercase(Locale.ROOT).contains(q)
                            || (it.email?: "").lowercase(Locale.ROOT).contains(q)
                            || (it.bio?: "").lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<TeacherEntity>
                filterData(newData)
            }
        }
    }
}