package com.workfort.pstuian.app.ui.faculty.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.faculty.listener.BatchClickEvent
import com.workfort.pstuian.app.ui.faculty.viewholder.BatchesMinViewHolder
import com.workfort.pstuian.app.ui.faculty.viewholder.BatchesViewHolder
import com.workfort.pstuian.databinding.RowBatchBinding
import com.workfort.pstuian.databinding.RowBatchMinBinding
import java.util.Locale

class BatchesAdapter(
    private val isExpandView: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private val data : MutableList<BatchEntity> = ArrayList()
    private val filteredData : MutableList<BatchEntity> = ArrayList()
    private var listener: BatchClickEvent? = null

    fun setData(data: MutableList<BatchEntity>) {
        this.data.clear()
        this.data.addAll(data)
        filterData(data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(data: MutableList<BatchEntity>) {
        // if new data is same as current data no need to do anything
        if(data.size <= itemCount && filteredData.toSet().minus(data.toSet()).isEmpty()) {
            return
        }

        filteredData.clear()
        filteredData.addAll(data)

        notifyDataSetChanged()
    }

    fun setListener(listener: BatchClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if(isExpandView) {
            return BatchesViewHolder(RowBatchBinding.inflate(inflater, parent, false))
        }

        return BatchesMinViewHolder(RowBatchMinBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val batch = filteredData[position]

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        if(isExpandView) {
            (holder as BatchesViewHolder).apply {
                bind(batch)
                binding.root.setOnClickListener {
                    run {
                        if(listener != null) listener?.onClickBatch(batch)
                    }
                }
            }
        } else {
            (holder as BatchesMinViewHolder).apply {
                bind(batch)
                binding.root.setOnClickListener {
                    run {
                        if(listener != null) listener?.onClickBatch(batch)
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val result: ArrayList<BatchEntity> = ArrayList()
                if(TextUtils.isEmpty(query)) {
                    result.addAll(data)
                } else {
                    val q = query.toString().lowercase(Locale.ROOT)
                    data.forEach {
                        if(it.name.lowercase(Locale.ROOT).contains(q)
                            || (it.title?: "").lowercase(Locale.ROOT).contains(q)
                            || it.session.lowercase(Locale.ROOT).contains(q))
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
                val newData = filteredResult?.values as ArrayList<BatchEntity>
                filterData(newData)
            }
        }
    }
}