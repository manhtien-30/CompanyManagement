package com.example.companymanagement.viewcontroller.fragment.task

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.model.task.UserTaskModel
import com.example.companymanagement.viewcontroller.adapter.TaskRecyclerViewAdapter

class Task_ItemDel( var taskInfo: UserTaskModel, val pos : Int) : DialogFragment() {
    private  lateinit var taskviewmodel: TaskViewModel
    private var adapter: TaskRecyclerViewAdapter = TaskRecyclerViewAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskviewmodel = ViewModelProvider(this.requireActivity()).get(TaskViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Thông báo")
        builder.setMessage("Bạn có chắc muốn xóa Task này?")
            .setPositiveButton("OK", DialogInterface.OnClickListener {
                    dialog, id ->
                taskviewmodel.deleteTask(taskInfo)
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = builder.create()
        return alert
    }
}