package com.example.companymanagement.viewcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.companymanagement.viewcontroller.fragment.signleave.managerleave.HistoryAll
import com.example.companymanagement.viewcontroller.fragment.signleave.managerleave.ResolveLeave

class ManagerLeaveViewAdapter(frag: Fragment) : FragmentStateAdapter(frag) {
    override fun getItemCount(): Int = 2
    override fun createFragment(pos: Int): Fragment {
        if (pos == 0) {
            return ResolveLeave()
        }
        return HistoryAll()
    }
}