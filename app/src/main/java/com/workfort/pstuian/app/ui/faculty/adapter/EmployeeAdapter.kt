package com.workfort.pstuian.app.ui.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.databinding.RowEmployeeBinding
import com.workfort.pstuian.app.ui.faculty.viewholder.EmployeeViewHolder
import com.workfort.pstuian.app.ui.faculty.listener.EmployeeClickEvent
import java.util.*
import kotlin.collections.ArrayList

class EmployeeAdapter : RecyclerView.Adapter<EmployeeViewHolder>(), Filterable {

    private val employees : MutableList<EmployeeEntity> = ArrayList()
    private val filteredEmployees : MutableList<EmployeeEntity> = ArrayList()
    private var listener: EmployeeClickEvent? = null

    fun setEmployees(employees: MutableList<EmployeeEntity>) {
        this.employees.clear()
        this.employees.addAll(employees)

        filter.filter("")
    }

    fun setListener(listener: EmployeeClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<EmployeeEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(employees)
                } else {
                    val q = query.toString().toLowerCase(Locale.ROOT)
                    employees.forEach {
                        if(it.name.toLowerCase(Locale.ROOT).contains(q)
                            || it.designation.toLowerCase(Locale.ROOT).contains(q)
                            || (it.department?: "").toLowerCase(Locale.ROOT).contains(q)
                            || (it.phone?:"").toLowerCase(Locale.ROOT).contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredEmployees.clear()
                filteredEmployees.addAll(filteredResult?.values as ArrayList<EmployeeEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredEmployees.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.row_employee, parent, false)
                as RowEmployeeBinding
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val student = filteredEmployees[position]

        holder.bind(student)
        holder.binding.root.setOnClickListener {
            run {
                if(listener != null) listener?.onClickEmployee(student)
            }
        }
    }
}