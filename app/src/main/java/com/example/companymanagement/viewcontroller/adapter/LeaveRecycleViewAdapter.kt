package com.example.companymanagement.viewcontroller.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.model.leave.LeaveInfoModel
import com.example.companymanagement.utils.customize.OnBindLeaveLisener
import com.example.companymanagement.utils.customize.OnBindOwnerLisener
import com.google.android.material.imageview.ShapeableImageView

class LeaveRecycleViewAdapter : RecyclerView.Adapter<LeaveHolder>() {

    var list: MutableList<LeaveInfoModel> = mutableListOf()
    private var leavebindinglisener: OnBindLeaveLisener? = null;
    private var ownerbindinglisener: OnBindOwnerLisener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LeaveHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return LeaveHolder(view)
    }

    fun setData(data: MutableList<LeaveInfoModel>) {
        this.list = data;
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LeaveHolder, position: Int) {
        holder.bind(list!![position])
        leavebindinglisener?.onBind(list!![position], holder)
        ownerbindinglisener?.onBind(list!![position].Owneruid, holder)

    }

    fun setOnBindOwner(e: OnBindOwnerLisener) {
        ownerbindinglisener = e;
    }

    fun setOnBindLeave(e: OnBindLeaveLisener) {
        leavebindinglisener = e;
    }
}

class LeaveHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    val avatar: ShapeableImageView = itemView.findViewById(R.id.item_leave_owner_avatar)
    val name: TextView = itemView.findViewById(R.id.item_leave_owner_name)
    var timecreate: TextView = itemview.findViewById(R.id.item_time_create)
    var dayleave: TextView = itemview.findViewById(R.id.item_leave_day)
    var check: TextView = itemview.findViewById(R.id.item_leave_check)

    fun bind(leave: LeaveInfoModel) {
        dayleave.text = leave.day_leave.toString()
        timecreate.text = DateUtils.getRelativeTimeSpanString(leave.CreateTime?.time!!)
        Log.d("abc", leave.check_Result.toString())
        when (leave.check_Result) {
            "reject" -> check.text = "Không chấp nhận"
            "undone" -> check.text = "Chưa duyệt"
            "accept" -> check.text = "Đã chấp nhận"
        }

    }
}