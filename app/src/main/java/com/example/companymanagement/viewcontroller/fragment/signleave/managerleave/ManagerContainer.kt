package com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.EmployeViewPagerApdapter
import com.example.companymanagement.viewcontroller.adapter.LeaveViewPagerAdapter
import com.example.companymanagement.viewcontroller.adapter.ManagerLeaveViewAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ManagerContainer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        var root = inflater.inflate(R.layout.fragment_manager, container, false)
        val pager = root.findViewById<ViewPager2>(R.id.managerleavepage)
        val tablayout = root.findViewById<TabLayout>(R.id.managerleaveTab)
        pager.adapter = ManagerLeaveViewAdapter(this);
        var tabtext = resources.getStringArray(R.array.manager_leave_tab_name)
        TabLayoutMediator(tablayout, pager) { tab, pos -> tab.text = tabtext[pos] }.attach()
        return root
    }


}