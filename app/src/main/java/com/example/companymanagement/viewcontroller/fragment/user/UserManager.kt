package com.example.companymanagement.viewcontroller.fragment.user

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.UserViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserManagerBottomSheet : BottomSheetDialogFragment() {


    //settup bottom sheet
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val parentLayout =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.skipCollapsed = true;
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }


    //viewcreate bottom sheet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.container_pageviewer, container, false)
        val pager = root.findViewById<ViewPager2>(R.id.container_viewpager)
        val tablayout = root.findViewById<TabLayout>(R.id.container_tablayout)
        pager.adapter = UserViewPagerAdapter(this);
        var tabtext = resources.getStringArray(R.array.user_manager_tab_name);
        TabLayoutMediator(tablayout, pager) { tab, pos -> tab.text = tabtext[pos] }.attach()
        // disable assign id for instance
        pager.isSaveEnabled = false;
        return root;
    }

    companion object {
        val instance = UserManagerBottomSheet();
        fun Instance(): UserManagerBottomSheet {
            return instance;
        }
    }

}