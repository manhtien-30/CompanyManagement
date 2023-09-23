package com.example.companymanagement.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.model.task.UserTaskModel
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate

fun interface OnTaskStatusClickListener {
    fun onClick(data: UserTaskModel, view: TaskHolder)
}

class UserTaskAdapter
    : RecyclerView.Adapter<TaskHolder>() {

    var list: MutableList<UserTaskModel> = mutableListOf()
    private var doneclick: OnTaskStatusClickListener? = null
    private var undoneclick: OnTaskStatusClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return TaskHolder(itemView)
    }

    fun setData(list: MutableList<UserTaskModel>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun clear() {
        this.list = mutableListOf()
        this.notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        val task: UserTaskModel = list[position]
        holder.Content.text = task.Content
        holder.Deadline.text = task.Deadline?.toHumanReadDate().toString()
        holder.Title.text = task.Title
        holder.SenderName.text = task.SenderName
        holder.SentDate.text = task.SentDate?.toHumanReadDate().toString()
        holder.Status.text = task.Status
        holder.DoneButton.setOnClickListener {
            doneclick?.onClick(list[position], holder)
            this.notifyItemChanged(position)
        }
        holder.UndoneButton.setOnClickListener {
            undoneclick?.onClick(list[position], holder)
            this.notifyItemChanged(position)
        }
    }

    fun setOnClickDone(e: OnTaskStatusClickListener) {
        doneclick = e;

    }

    fun setOnClickUndone(e: OnTaskStatusClickListener) {
        undoneclick = e;
    }

    override fun getItemCount(): Int = list.size

}

class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val Content: TextView = itemView.findViewById(R.id.task_content)
    val Deadline: TextView = itemView.findViewById(R.id.task_deadline)
    val SenderName: TextView = itemView.findViewById(R.id.task_sender)
    val SentDate: TextView = itemView.findViewById(R.id.task_sentDate)
    val Status: TextView = itemView.findViewById(R.id.task_status)
    val Title: TextView = itemView.findViewById(R.id.task_title)
    val ContainerButton: LinearLayoutCompat = itemView.findViewById(R.id.task_button_container)
    val DoneButton: Button = itemView.findViewById(R.id.task_done)
    val UndoneButton: Button = itemView.findViewById(R.id.task_undone)

    var expanded: Boolean
        get() {
            return ContainerButton.visibility == View.VISIBLE
        }
        set(value) {
            if (value) ContainerButton.visibility = View.VISIBLE else ContainerButton.visibility =
                View.GONE
        }

    init {
        itemView.setOnClickListener {
            expanded = !expanded
        }
    }

    fun hideButton() {
        expanded = false;
    }
}