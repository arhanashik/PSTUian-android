package com.workfort.pstuian.app.ui.blooddonationrequest.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.blooddonationrequest.viewholder.BloodDonationRequestViewHolder
import com.workfort.pstuian.databinding.RowBloodDonationRequestBinding
import java.util.Locale

class BloodDonationRequestAdapter : RecyclerView.Adapter<BloodDonationRequestViewHolder>(),
    Filterable {

    private val data : MutableList<BloodDonationRequestEntity> = ArrayList()
    private val filteredData : MutableList<BloodDonationRequestEntity> = ArrayList()

    fun setData(data: MutableList<BloodDonationRequestEntity>) {
        this.data.clear()
        this.data.addAll(data)
        filterData(data)
    }

    fun addData(data: MutableList<BloodDonationRequestEntity>) {
        if(data.isEmpty()) return

        val newData = if(itemCount == 0) data else data.toSet().minus(this.data.toSet())
        if(newData.isEmpty()) return

        val startPosition = itemCount
        this.data.addAll(newData)
        this.filteredData.addAll(newData)

        if(newData.size == 1) notifyItemInserted(startPosition)
        else notifyItemRangeInserted(startPosition, newData.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<BloodDonationRequestEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun clear() {
        val lastPosition = itemCount
        this.data.clear()
        this.filteredData.clear()
        notifyItemRangeRemoved(0, lastPosition)
    }

    override fun getItemCount(): Int = filteredData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodDonationRequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BloodDonationRequestViewHolder(
            RowBloodDonationRequestBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BloodDonationRequestViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(filteredData[position])
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<BloodDonationRequestEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.name.lowercase(Locale.ROOT).contains(q)
                            || (it.bloodGroup).lowercase(Locale.ROOT).contains(q)
                            || (it.beforeDate).lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<BloodDonationRequestEntity>
                filterData(newData)
            }
        }
    }
}