package com.workfort.pstuian.app.ui.home.faculty.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.databinding.RowDonorBinding
import com.workfort.pstuian.app.ui.home.faculty.holder.DonorsViewHolder
import com.workfort.pstuian.app.ui.home.faculty.listener.DonorClickEvent

class DonorsAdapter : RecyclerView.Adapter<DonorsViewHolder>(), Filterable {

    private val donors : MutableList<DonorEntity> = ArrayList()
    private val filteredDonors : MutableList<DonorEntity> = ArrayList()
    private var listener: DonorClickEvent? = null

    fun setDonors(donors: MutableList<DonorEntity>) {
        this.donors.clear()
        this.donors.addAll(donors)

        filter.filter("")
    }

    fun setListener(listener: DonorClickEvent) {
        this.listener = listener
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<DonorEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(donors)
                }else {
                    val q = query.toString().toLowerCase()
                    donors.forEach {
                        if(it.name!!.toLowerCase().contains(q)
                            || it.info!!.toLowerCase().contains(q)
                            || it.reference!!.toLowerCase().contains(q))
                            result.add(it)
                    }
                }

                val filteredResult = FilterResults()
                filteredResult.count = result.size
                filteredResult.values = result

                return filteredResult
            }

            override fun publishResults(query: CharSequence?, filteredResult: FilterResults?) {
                filteredDonors.clear()
                filteredDonors.addAll(filteredResult?.values as ArrayList<DonorEntity>)

                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredDonors.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(inflater, R.layout.row_donor, parent, false)
                as RowDonorBinding
        return DonorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonorsViewHolder, position: Int) {
        val batch = filteredDonors[position]

        holder.bind(batch)
        holder.binding.root.setOnClickListener { view ->
            run {
                if(listener != null) listener?.onClickDonor(batch)
            }
        }
    }
}