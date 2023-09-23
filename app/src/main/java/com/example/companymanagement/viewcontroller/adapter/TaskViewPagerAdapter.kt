package com.example.companymanagement.viewcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.companymanagement.viewcontroller.fragment.task.TaskOverall
import com.example.companymanagement.viewcontroller.fragment.task.TaskCreate
import com.example.companymanagement.viewcontroller.fragment.task.TaskList

class TaskViewPagerAdapter (frag: Fragment) :
    FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = 3

    override fun createFragment(pos: Int): Fragment {
        if (pos == 0) {
            return TaskOverall();
        }
        if (pos == 1) {
            return TaskList();
        }
        return TaskCreate();
    }
}