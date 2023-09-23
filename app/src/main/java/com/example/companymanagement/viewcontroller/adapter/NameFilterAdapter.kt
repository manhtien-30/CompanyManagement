package com.example.companymanagement.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R


class NameFilterAdapter(private val namelist: ArrayList<String>, private val listener: OnItemClickListener):
    RecyclerView.Adapter<NameFilterAdapter.NameViewHolder>() {

    override fun getItemCount(): Int = namelist.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_name_filter, parent, false)
        return NameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        val name = namelist[position]

        // ToDo : get name
        holder.name.text = name


        //ToDo bind avatar : holder.avatar
    }

    fun addNames(names: List<String>) {
        this.namelist.apply {
            clear()
            addAll(names)
        }
        notifyDataSetChanged()
    }

    inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.findViewById(R.id.nameView)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(position: Int)
    }
}
