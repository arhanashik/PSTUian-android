package com.workfort.pstuian.app.ui.donors.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.app.ui.donors.viewholder.DonorsViewHolder
import com.workfort.pstuian.app.ui.faculty.listener.DonorClickEvent
import com.workfort.pstuian.databinding.RowDonorBinding
import java.util.*
import kotlin.collections.ArrayList

class DonorsAdapter : RecyclerView.Adapter<DonorsViewHolder>(), Filterable {

    private val data : MutableList<DonorEntity> = ArrayList()
    private val filteredData : MutableList<DonorEntity> = ArrayList()
    private var listener: DonorClickEvent? = null

    fun setData(data: MutableList<DonorEntity>) {
        this.data.clear()
        this.data.addAll(data)
        filterData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<DonorEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun setListener(listener: DonorClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowDonorBinding.inflate(inflater, parent, false)
        return DonorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonorsViewHolder, position: Int) {
        val batch = filteredData[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(batch)
        holder.binding.root.setOnClickListener {
            run {
                listener?.onClickDonor(batch)
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<DonorEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if((it.name?: "").lowercase(Locale.ROOT).contains(q)
                            || (it.info?: "").lowercase(Locale.ROOT).contains(q)
                            || it.reference.lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<DonorEntity>
                filterData(newData)
            }
        }
    }
}