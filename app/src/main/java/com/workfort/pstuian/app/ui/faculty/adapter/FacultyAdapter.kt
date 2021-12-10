package com.workfort.pstuian.app.ui.faculty.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.ui.faculty.listener.FacultyClickEvent
import com.workfort.pstuian.app.ui.faculty.viewholder.FacultyViewHolder
import com.workfort.pstuian.databinding.RowFacultyBinding

class FacultyAdapter : RecyclerView.Adapter<FacultyViewHolder>() {
    private val faculties : MutableList<FacultyEntity> = ArrayList()
    private var listener: FacultyClickEvent? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setFaculties(faculties: MutableList<FacultyEntity>) {
        this.faculties.clear()
        this.faculties.addAll(faculties)
        notifyDataSetChanged()
    }

    fun setListener(listener: FacultyClickEvent) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return faculties.size
    }

    fun getItems() = faculties

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowFacultyBinding.inflate(inflater, parent, false)
        return FacultyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        val faculty = faculties[position]
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context,
            R.anim.anim_item_insert)
        holder.bind(faculty)
        holder.binding.root.setOnClickListener {
            listener?.onClickFaculty(faculty)
        }
    }
}