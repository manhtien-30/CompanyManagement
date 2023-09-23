package com.example.companymanagement.viewcontroller.fragment.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.companymanagement.R
import com.example.companymanagement.utils.UtilsFuntion.convertDPToPX
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.BaselineLayout


class Navigation : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        com.google.android.material.R.layout.design_bottom_navigation_item
        val navcontroller = this.requireActivity().findNavController(R.id.activity_container_fragment)
        val bottomnav = view?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomnav?.setupWithNavController(navcontroller)
        var menuview = bottomnav?.getChildAt(0) as BottomNavigationMenuView
        var first = menuview.getChildAt(0) as BottomNavigationItemView

        setupHightLightButton(first)
        bottomnav.setOnNavigationItemSelectedListener {

            if (it.itemId != R.id.main_home) {

                first.background =
                    context?.resources?.getDrawable(R.drawable.bg_item_bottom_navigation_unhightlight)
            } else {
                first.background =
                    context?.resources?.getDrawable(R.drawable.bg_item_bottom_navigation_hightlight)
            }
            return@setOnNavigationItemSelectedListener true
        }
        bottomnav?.setupWithNavController(navcontroller)

    }

    @SuppressLint("RestrictedApi")
    fun setupHightLightButton(first: BottomNavigationItemView) {
        first.background =
            context?.resources?.getDrawable(R.drawable.bg_item_bottom_navigation_hightlight)
        first.setIconTintList(context?.resources?.getColorStateList(R.color.white))
        first.setTextColor(context?.resources?.getColorStateList(R.color.white))
    }
}