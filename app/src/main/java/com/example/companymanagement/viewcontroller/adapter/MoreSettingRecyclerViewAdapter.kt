package com.example.companymanagement.viewcontroller.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.fragment.mainmore.MoreItem


class MoreSettingRecyclerViewAdapter(
    private val values: List<MoreItem>,
) : RecyclerView.Adapter<SettingItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_more_setting, parent, false)
        return SettingItemHolder(view)
    }


    override fun getItemCount(): Int = values.size


    override fun onBindViewHolder(holder: SettingItemHolder, position: Int) {
        if (values[position].ClickListener != null) {
            holder.NameButton.setOnClickListener(values[position].ClickListener)
        }
        if (values[position].drawable != null) {
            holder.NameButton.setCompoundDrawablesWithIntrinsicBounds(values[position].drawable,
                null,
                null,
                null)
        }
        holder.NameButton.setText(values[position].name);
    }
}

class SettingItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var NameButton: Button;

    init {
        this.NameButton = itemView.findViewById(R.id.item_more_btn)
    }
}