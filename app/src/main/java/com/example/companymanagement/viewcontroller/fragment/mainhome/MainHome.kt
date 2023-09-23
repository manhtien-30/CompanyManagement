package com.example.companymanagement.viewcontroller.fragment.mainhome

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.utils.RecycleViewCalculate
import com.example.companymanagement.viewcontroller.adapter.HomeGridViewApdapter
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserRoleViewModel


class MainHome : Fragment() {
    lateinit var adminlist: MutableList<HomeGridViewViewModel>
    lateinit var userlist: MutableList<HomeGridViewViewModel>
    lateinit var rolemodel: UserRoleViewModel;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        createdata()
        rolemodel = ViewModelProvider(requireActivity()).get(UserRoleViewModel::class.java)
        return inflater.inflate(R.layout.fragment_main_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calculator = RecycleViewCalculate(requireContext(),
            R.layout.item_cardview_button)
        val adminlayout = view.findViewById<CardView>(R.id.admin_container)
        rolemodel.isAdmin.observe(this.viewLifecycleOwner) {
            if (it == true) adminlayout.visibility = View.VISIBLE else adminlayout.visibility =
                View.GONE
        }
        //
        val adminrc = view.findViewById<RecyclerView>(R.id.admin_rc).apply {
            adapter = HomeGridViewApdapter(requireActivity(), adminlist)
            layoutManager = GridLayoutManager(context, calculator.calculateNoOfColumns());
            addItemDecoration(RecycleViewCalculate.SpacingDecoration(calculator.calculateSpacing()))
        }

        val userrc = view.findViewById<RecyclerView>(R.id.user_rc).apply {
            adapter = HomeGridViewApdapter(requireActivity(), userlist)
            layoutManager = GridLayoutManager(context, calculator.calculateNoOfColumns());
            addItemDecoration(RecycleViewCalculate.SpacingDecoration(calculator.calculateSpacing()))
        }
    }


    fun createdata() {
        adminlist = mutableListOf(HomeGridViewViewModel(R.drawable.ic_user_manager,
            R.id.employee_manager,
            "Quản lý nhân viên", Color.valueOf(Color.CYAN)),
            HomeGridViewViewModel(
                R.drawable.ic_clipboard,
                R.id.task_manager,
                "Quản lý task", Color.valueOf(Color.RED)),
            HomeGridViewViewModel(R.drawable.ic_stamp,
                R.id.leave_manager,
                "Xét duyệt đơn nghỉ phép", Color.valueOf(Color.GREEN)),
            HomeGridViewViewModel(R.drawable.ic_salary,
                R.id.manager_salary,
                "Quản lý lương"))

        userlist = mutableListOf(
            HomeGridViewViewModel(R.drawable.ic_qr_code,
                R.id.checkin_qrscanner,
                "Checking", Color.valueOf(Color.BLUE)),
            HomeGridViewViewModel(R.drawable.ic_salary_manager,
                R.id.user_salary,
                "Lương của tôi", Color.valueOf(Color.YELLOW), Color.valueOf(Color.DKGRAY)),
            HomeGridViewViewModel(R.drawable.ic_statistics,
                R.id.leaderboard,
                "Bảng xếp hạng", Color.valueOf(Color.DKGRAY)),
            HomeGridViewViewModel(R.drawable.ic_absent,
                R.id.leave_sign,
                "Xin nghỉ phếp", Color.valueOf(Color.MAGENTA))
        )
    }
}