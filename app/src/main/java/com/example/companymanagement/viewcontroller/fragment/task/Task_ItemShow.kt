package com.example.companymanagement.viewcontroller.fragment.employe

import android.R.attr.data
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.model.task.UserTaskModel
import com.example.companymanagement.utils.DateParser
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate
import com.example.companymanagement.viewcontroller.fragment.task.TaskViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat


class Task_ItemShow( var taskInfo: UserTaskModel) : DialogFragment() {

    val format: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss")
    private  lateinit var taskviewmodel: TaskViewModel
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskviewmodel = ViewModelProvider(this.requireActivity()).get(TaskViewModel::class.java)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_dialog_task, LinearLayout(activity), false)

        val dlg = Dialog(view.context)
        dlg.setContentView(view)
        val tTitle = dlg.findViewById<EditText>(R.id.dl_task_title)
        val tContent = dlg.findViewById<EditText>(R.id.dl_task_content)
        val tSender = dlg.findViewById<EditText>(R.id.dl_task_sender)
        val tReceiver = dlg.findViewById<EditText>(R.id.dl_task_receiver)
        val tDeadline = dlg.findViewById<EditText>(R.id.dl_task_deadline)
        val tCreateDate = dlg.findViewById<EditText>(R.id.dl_task_sentdate)
        val tStatus = dlg.findViewById<CheckBox>(R.id.dl_task_status)
        val edit = dlg.findViewById<ImageView>(R.id.edit)

        tTitle.setText(taskInfo.Title)
        tContent.setText(taskInfo.Content)
        tSender.setText(taskInfo.SenderName)
        tDeadline.setText(format.format(taskInfo.Deadline))
        tCreateDate.setText(format.format(taskInfo.SentDate))
        if (taskInfo.Status == "Undone")
            tStatus.isChecked = false
        else tStatus.isChecked = true
        var data : String = String()
        for (tag in taskInfo.NameReceiver!!) {
            data += tag + ","
        }
        tReceiver.setText(data.dropLast(1))

        fun setEdit (check : Boolean) {
            tTitle.isEnabled = check
            tContent.isEnabled = check
            tDeadline.isEnabled = check
            tStatus.isClickable = check
        }


        edit.setOnClickListener {
            if (edit.tag == "cant_change") {
                Toast.makeText(context, "Cho phép chỉnh sửa thông tin", Toast.LENGTH_SHORT)
                    .show()
                edit.tag = "can_change"
                edit.setImageResource(R.drawable.ic_check)
                setEdit(true)
            } else if (edit.tag == "can_change") {
                val checked = format.parse(tDeadline.text.toString())
                val creatTime = taskInfo.SentDate
                if (checked?.after(creatTime) == true) {
                    edit.tag = "cant_change"
                    edit.setImageResource(R.drawable.ic_edit)
                    setEdit(false)
                    try {
                        var task = taskInfo.apply {
                            Title = tTitle.text.toString()
                            Content = tContent.text.toString()
                            Deadline = format.parse(tDeadline.text.toString())
                            if (tStatus.isChecked == false)
                                Status = "Undone"
                            else Status = "Completed"
                        }
                        taskviewmodel.updateTask(task)

                        var t = taskviewmodel.TaskList.value?.find {
                            it.taskid == task.taskid
                        }.apply {
                            this?.Title = task.Title
                            this?.Content = task.Content
                            this?.Deadline = task.Deadline
                            this?.Status = task.Status

                        }
                        taskviewmodel.TaskList.postValue(
                            taskviewmodel.TaskList.value
                        )
                        Toast.makeText(context, "Hoàn tất chỉnh sửa thông tin", Toast.LENGTH_LONG)
                            .show()
                    } catch (ex: Exception) {
                        Toast.makeText(context, ex.message!!, Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "Thời gian không hợp lệ, xin vui lòng kiểm tra lại!", Toast.LENGTH_LONG)
                        .show()
                }

            }
        }
        return dlg
    }
    fun update(checked: Boolean) {
        var result = String()
        if (checked == false)
            result = "Undone"
        else result = "Completed"
        db.collection("task")
            .document(taskInfo.taskid!!)
            .update("status",result);
        taskInfo.Status = result
    }
}
