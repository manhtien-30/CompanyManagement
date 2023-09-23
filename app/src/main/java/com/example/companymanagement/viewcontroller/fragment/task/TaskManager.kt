package com.example.companymanagement.viewcontroller.fragment.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.EmployeViewPagerApdapter
import com.example.companymanagement.viewcontroller.adapter.TaskViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TaskManager : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.container_pageviewer, container, false)
        val pager = root.findViewById<ViewPager2>(R.id.container_viewpager)
        val tablayout = root.findViewById<TabLayout>(R.id.container_tablayout)
        pager.adapter = TaskViewPagerAdapter(this);
        var tabtext = resources.getStringArray(R.array.task_manager_tab_name);
        TabLayoutMediator(tablayout, pager) { tab, pos -> tab.text = tabtext[pos] }.attach()
        return root;
    }
}