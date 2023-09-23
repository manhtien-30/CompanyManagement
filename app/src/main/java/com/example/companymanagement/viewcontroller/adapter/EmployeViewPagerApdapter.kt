package com.example.companymanagement.viewcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.companymanagement.viewcontroller.fragment.employe.EmployeList
import com.example.companymanagement.viewcontroller.fragment.employe.EmployeRegister

class EmployeViewPagerApdapter(frag: Fragment) :
    FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = 2

    override fun createFragment(pos: Int): Fragment {
        if (pos == 0) {
            return EmployeRegister();
        }
        return EmployeList();
    }
}