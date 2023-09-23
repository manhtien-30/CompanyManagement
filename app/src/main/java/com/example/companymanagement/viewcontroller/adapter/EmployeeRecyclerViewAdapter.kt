package com.example.companymanagement.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.companymanagement.R
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.utils.UtilsFuntion
import com.example.companymanagement.viewcontroller.fragment.employe.Employe_ItemShow

import com.google.android.material.imageview.ShapeableImageView

class EmployeeRecyclerViewAdapter() :
    RecyclerView.Adapter<EmployeeRecyclerViewAdapter.EmployeeHolder>() {

    var list: MutableList<UserInfoModel>? = null

    class EmployeeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fName = itemView.findViewById<TextView>(R.id.employe_fullname)
        val contactmail = itemView.findViewById<TextView>(R.id.employe_mail)
        val epos = itemView.findViewById<TextView>(R.id.employe_pos)
        val avatar = itemView.findViewById<ShapeableImageView>(R.id.employe_avatar)


        fun bind(employee: UserInfoModel) {
            fName.text = employee.Name
            contactmail.text = employee.Email
            epos.text = employee.Position
            val dp =
                UtilsFuntion.convertDPToPX(32.0F, itemView.context).toInt()
            Glide.with(itemView)
                .load(employee.AvatarURL)
                .override(dp, dp)
                .centerCrop()
                .placeholder(CircularProgressDrawable(itemView.context).apply { start() })
                .error(R.drawable.ic_default_avatar)
                .into(avatar)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardview_employe, parent, false)
        return EmployeeHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeHolder, position: Int) {
        holder.bind(list!![position])
        holder.itemView.setOnClickListener {

            val dlg = Employe_ItemShow(list!![position])
            dlg.show((holder.itemView.context as FragmentActivity).supportFragmentManager.beginTransaction(),
                "itemshow")


        }
    }

    override fun getItemCount(): Int = list?.size ?: 0;
    fun setData(data: MutableList<UserInfoModel>) {
        this.list = data
        this.notifyDataSetChanged()
    }
//    fun setNewData(data: EmployeeModel) {
//        this.list?.add(data)
//        this.notifyItemInserted(list!!.lastIndex)
//    }
}
