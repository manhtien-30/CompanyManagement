package com.example.companymanagement.viewcontroller.fragment.mainmore

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.MoreSettingRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import java.io.Console

class MainMore : Fragment() {
    var auth = FirebaseAuth.getInstance();
    var moreItemList: MutableList<MoreItem> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moreItemList.add(
            MoreItem.Builder(context).setName("Đăng xuất").setDrawableFromID(R.drawable.ic_logout)
                .setOnClickListener {
                    auth.signOut()
                }.create()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_more, container, false)
        val recycle = view.findViewById<RecyclerView>(R.id.list)
        // Set the adapter
        recycle.layoutManager = LinearLayoutManager(context);
        recycle.adapter = MoreSettingRecyclerViewAdapter(moreItemList);
        return view
    }
}