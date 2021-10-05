package com.workfort.pstuian.app.ui.donors.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.app.ui.donors.viewholder.DonorsViewHolder
import com.workfort.pstuian.app.ui.faculty.listener.DonorClickEvent
import com.workfort.pstuian.databinding.RowDonorBinding
import java.util.*
import kotlin.collections.ArrayList

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
                } else {
                    val q = query.toString().toLowerCase(Locale.ROOT)
                    donors.forEach {
                        if((it.name?: "").toLowerCase(Locale.ROOT).contains(q)
                            || (it.info?: "").toLowerCase(Locale.ROOT).contains(q)
                            || it.reference.toLowerCase(Locale.ROOT).contains(q))
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
                @Suppress("UNCHECKED_CAST")
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
        val binding = RowDonorBinding.inflate(inflater, parent, false)
        return DonorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonorsViewHolder, position: Int) {
        val batch = filteredDonors[position]

        holder.bind(batch)
        holder.binding.root.setOnClickListener {
            run {
                listener?.onClickDonor(batch)
            }
        }
    }
}