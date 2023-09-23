package com.example.companymanagement.viewcontroller.fragment.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.ForgotPassPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ForgotPassManager: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tablayout_forgotpass, container, false)
        val pager = root.findViewById<ViewPager2>(R.id.forgotpass_manager_viewpager)
        pager.adapter = ForgotPassPagerAdapter(this);
        return root;
    }
}