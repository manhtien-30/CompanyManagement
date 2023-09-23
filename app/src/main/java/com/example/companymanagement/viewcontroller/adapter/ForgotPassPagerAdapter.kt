package com.example.companymanagement.viewcontroller.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.companymanagement.viewcontroller.fragment.forgotpassword.ForgotPass

class ForgotPassPagerAdapter(frag: Fragment) :
    FragmentStateAdapter(frag) {
    override fun getItemCount(): Int = 1

    override fun createFragment(pos: Int): Fragment {
        return ForgotPass()
    }
}
