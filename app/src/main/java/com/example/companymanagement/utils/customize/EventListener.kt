package com.example.companymanagement.utils.customize

import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.model.leave.LeaveInfoModel
import com.example.companymanagement.viewcontroller.adapter.LeaveHolder

fun interface OnButtonClickListener {
    fun onClick(tweetid: String);
}

fun interface OnBindOwnerLisener {
    fun onBind(ownerid: String, vh: RecyclerView.ViewHolder);
}

fun interface OnBindLeaveLisener {
    fun onBind(info: LeaveInfoModel, vh: LeaveHolder);
}
