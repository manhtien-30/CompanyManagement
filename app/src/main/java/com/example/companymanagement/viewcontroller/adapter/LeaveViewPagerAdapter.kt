package com.example.companymanagement.viewcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave.History
import com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave.CreateLeave

class LeaveViewPagerAdapter(frag: Fragment) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = 2
    override fun createFragment(pos: Int): Fragment {
        if (pos == 0) {
            return CreateLeave()
        }
        return History()
    }
}