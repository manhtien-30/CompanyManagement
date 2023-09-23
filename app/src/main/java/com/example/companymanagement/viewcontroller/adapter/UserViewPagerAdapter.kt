package com.example.companymanagement.viewcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.companymanagement.viewcontroller.fragment.user.UserInfo
import com.example.companymanagement.viewcontroller.fragment.user.userstatictis.UserStatictis
import com.example.companymanagement.viewcontroller.fragment.user.task.UserTask


class UserViewPagerAdapter(frag: Fragment) :
    FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = 3

    override fun createFragment(pos: Int): Fragment {
        return when (pos) {
            0 -> UserStatictis()
            1 -> UserTask()
            else -> UserInfo()
        }
    }
}