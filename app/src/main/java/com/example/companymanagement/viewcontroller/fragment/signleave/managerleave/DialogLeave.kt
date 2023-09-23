package com.example.companymanagement.viewcontroller.fragment.signleave.managerleave

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.model.leave.LeaveInfoModel
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate
import com.example.companymanagement.viewcontroller.fragment.mainworkspace.ListUserParticipantViewModel
import com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave.LeaveViewModel
import com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave.ManagerLeaveViewModel

class DialogLeave(var leaveInfo: LeaveInfoModel, val buttonvisible: Boolean = false) :
    DialogFragment() {

     var viewmodel = ManagerLeaveViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_accept_leave, LinearLayout(activity), false)

        val dialog = Dialog(view.context)
        dialog.setContentView(view)

        val user_name = view.findViewById<TextView>(R.id.bt_username)
        val day_leave = view.findViewById<TextView>(R.id.bt_dayleave)
        val daycreate = view.findViewById<TextView>(R.id.bt_daycreate)
        val daystart = view.findViewById<TextView>(R.id.bt_daystart)
        val reason = view.findViewById<TextView>(R.id.bt_reason)
        val btn_refuse: Button = view.findViewById(R.id.btn_leave_refuse)
        val btn_accept: Button = view.findViewById(R.id.btn_leave_accept)

        //set data
        user_name.text = leaveInfo.name
        daycreate.text = leaveInfo.CreateTime?.toHumanReadDate()
        reason.text = leaveInfo.reason
        day_leave.text = leaveInfo.day_leave.toString()
        daystart.text = leaveInfo.time_start
        if (!buttonvisible) {
            btn_accept.visibility = View.GONE
            btn_refuse.visibility = View.GONE
        } else {
            btn_accept.visibility = View.VISIBLE
            btn_refuse.visibility = View.VISIBLE
            btn_accept.setOnClickListener {
                viewmodel.accept(leaveInfo)
                this.dismiss()
            }
            //
            btn_refuse.setOnClickListener {
                viewmodel.reject(leaveInfo)
                this.dismiss()
            }
        }
        //button chap nhan don
        return dialog
    }

}
