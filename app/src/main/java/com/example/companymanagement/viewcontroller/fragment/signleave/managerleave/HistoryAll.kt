package com.example.companymanagement.viewcontroller.fragment.signleave.managerleave

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.companymanagement.R
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.utils.UtilsFuntion
import com.example.companymanagement.utils.customize.EndlessScrollRecyclListener
import com.example.companymanagement.viewcontroller.adapter.LeaveHolder
import com.example.companymanagement.viewcontroller.adapter.LeaveRecycleViewAdapter
import com.example.companymanagement.viewcontroller.fragment.mainworkspace.ListUserParticipantViewModel
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave.ManagerLeaveViewModel


class HistoryAll : Fragment() {

    private var managerLeavemodel: ManagerLeaveViewModel = ManagerLeaveViewModel()
    private lateinit var userlistppviewmodel: ListUserParticipantViewModel;
    private lateinit var userinfoviewmodel: UserInfoViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        managerLeavemodel.getLeaveResolve()
        userlistppviewmodel =
            ViewModelProvider(this.requireActivity()).get(ListUserParticipantViewModel::class.java)
        userinfoviewmodel =
            ViewModelProvider(this.requireActivity()).get(UserInfoViewModel::class.java)

        return inflater.inflate(R.layout.fragment_manager_leave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView = view.findViewById<RecyclerView>(R.id.listmanager)
        var adapter = LeaveRecycleViewAdapter()
        val layout = LinearLayoutManager(context)
        layout.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
        managerLeavemodel.LeaveList.observe(viewLifecycleOwner) {

            adapter.setData(it)
            adapter.notifyDataSetChanged()

        }
        adapter.setOnBindLeave { leave, holder ->
            holder.itemView.setOnClickListener {

                DialogLeave(leave, false).show(this.parentFragmentManager, "itemshow")
            }
        }

        adapter.setOnBindOwner { uuid, vh: RecyclerView.ViewHolder ->
            if (vh is LeaveHolder) {
                fun bind(user: UserInfoModel?, vh: LeaveHolder) {
                    val dp = UtilsFuntion.convertDPToPX(32.0F, context).toInt()
                    Glide.with(this).load(user?.AvatarURL)
                        .placeholder(CircularProgressDrawable(requireContext()).apply { start() })
                        .override(dp, dp)
                        .centerCrop()
                        .error(R.drawable.ic_default_avatar)
                        .into(vh.avatar)
                    vh.name.text = user?.Name
                }
                if (userlistppviewmodel.UserList.value?.containsKey(uuid) == true) {
                    bind(userlistppviewmodel.UserList.value?.get(uuid), vh);
                } else {
                    userlistppviewmodel.appendUser(uuid).observe(viewLifecycleOwner) {
                        bind(it, vh);
                    }
                }

            }
        }
        recyclerView.addOnScrollListener(object : EndlessScrollRecyclListener() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                managerLeavemodel.lazyLoadResolve().observe(viewLifecycleOwner) {
                    adapter.notifyItemRangeInserted(adapter.list?.lastIndex!!, it)
                }
                Log.d("Load nore", "load More")
            }
        })
    }
}