package com.example.companymanagement.viewcontroller.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.companymanagement.R


class NameListAdapter(context: Context, list: ArrayList<String>) :
    ArrayAdapter<String>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val empName = getItem(position)
        var convert = convertView
        if (convert == null) {
            convert =
                LayoutInflater.from(parent.context).inflate(R.layout.item_name_filter, parent, false)
            val viewHolder = ViewHolder(convertView!!)
            convert!!.tag = viewHolder
        }

        val viewHolder = convert!!.tag as ViewHolder
        if (empName != null) {
            viewHolder.bindName(empName)
        }

        return convertView!!
    }

    override fun getFilter(): Filter {
        return nameFilter
    }

    private val nameFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults? {
            val results = FilterResults()
            val suggestionList: MutableList<String> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                suggestionList.addAll(list)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in list) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        suggestionList.add(item)
                    }
                }
            }
            results.values = suggestionList
            results.count = suggestionList.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            //TODO do stuff
            val empNameList = results.values as List<String>
            if (empNameList != null) {
                clear()
                addAll(empNameList)
            }
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return super.convertResultToString(resultValue)
        }
    }

    internal class ViewHolder (itemView : View){
        var name: TextView = itemView.findViewById(R.id.nameView)

        fun bindName(inputName: String?) {
            name.text = inputName
        }
    }

}