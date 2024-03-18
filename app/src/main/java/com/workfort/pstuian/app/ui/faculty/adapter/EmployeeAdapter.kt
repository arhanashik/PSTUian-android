package com.workfort.pstuian.app.ui.faculty.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.faculty.listener.EmployeeClickEvent
import com.workfort.pstuian.app.ui.faculty.viewholder.EmployeeViewHolder
import com.workfort.pstuian.databinding.RowEmployeeBinding
import java.util.Locale

class EmployeeAdapter : RecyclerView.Adapter<EmployeeViewHolder>(), Filterable {

    private val data : MutableList<EmployeeEntity> = ArrayList()
    private val filteredData : MutableList<EmployeeEntity> = ArrayList()
    private var listener: EmployeeClickEvent? = null

    fun setData(employees: MutableList<EmployeeEntity>) {
        this.data.clear()
        this.data.addAll(employees)
        filterData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<EmployeeEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun setListener(listener: EmployeeClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowEmployeeBinding.inflate(inflater, parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val student = filteredData[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickEmployee(student)
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<EmployeeEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.name.lowercase(Locale.ROOT).contains(q)
                            || it.designation.lowercase(Locale.ROOT).contains(q)
                            || (it.department?: "").lowercase(Locale.ROOT).contains(q)
                            || (it.phone?:"").lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<EmployeeEntity>
                filterData(newData)
            }
        }
    }
}