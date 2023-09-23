package com.example.companymanagement.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.model.task.UserTaskModel
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate
import com.example.companymanagement.viewcontroller.fragment.employe.Task_ItemShow
import com.example.companymanagement.viewcontroller.fragment.task.Task_ItemDel

class TaskRecyclerViewAdapter: RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskHolder>()  {
    var list: MutableList<UserTaskModel>? = null
    class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val taskStatus = itemView.findViewById<CheckBox>(R.id.item_task_cb)
        val taskTitle = itemView.findViewById<TextView>(R.id.item_task_name)
        val taskSender = itemView.findViewById<TextView>(R.id.item_task_sender)
        val taskDl = itemView.findViewById<TextView>(R.id.item_task_deadline)
        val bdel = itemView.findViewById<ImageView>(R.id.item_task_del)
        val cb = itemView.findViewById<CheckBox>(R.id.item_task_cb)
        fun bind(task: UserTaskModel){
            if(task.Status == "Undone") taskStatus.isChecked = false
            else taskStatus.isChecked = true
            taskTitle.text = task.Title
            taskSender.text = task.SenderName
            taskDl.text = task.Deadline?.toHumanReadDate()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cardview_task,parent,false)
        return TaskHolder(view)
    }
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(list!![position])

        holder.itemView.setOnClickListener {
            val dlg = Task_ItemShow(list!![position])
            dlg.show((holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction(),
                "itemshow")
        }
        holder.bdel.setOnClickListener {
            val dlg = Task_ItemDel(list!![position], position)
            dlg.show((holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction(),
                "itemshow")
        }
        holder.cb.setOnClickListener {
            Task_ItemShow(list!![position]).update(holder.cb.isChecked)
        }

    }

    override fun getItemCount(): Int = list?.size ?: 0;

    fun setData(data: MutableList<UserTaskModel>) {
        this.list = data
        this.notifyDataSetChanged()
    }


}