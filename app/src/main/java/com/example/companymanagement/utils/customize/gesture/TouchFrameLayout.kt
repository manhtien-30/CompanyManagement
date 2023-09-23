package com.example.companymanagement.utils.customize.gesture

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.core.view.ScrollingView
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class TouchFrameLayout(context: Context, attr: AttributeSet) : FrameLayout(context, attr) {

    private var scrollablelist: MutableList<View> = mutableListOf()


    fun findScrollableView(view: View) {
        if (view.isScrollContainer) {
            scrollablelist.add(view);
            return;
        } else if (view is ViewGroup) {
            view.children.forEach {
                findScrollableView(it)
            }
        }
    }


    override fun onViewAdded(child: View) {
        super.onViewAdded(child)

    }

    override fun onViewRemoved(child: View) {
        super.onViewRemoved(child)
    }

//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//
//        Log.d("intercept", scrollablelist.size.toString())
//
//        super.onInterceptTouchEvent(ev)
//        findScrollableView(this.rootView)
//        for (view: View in scrollablelist) {
//
//            var rect = Rect()
//            view.getDrawingRect(rect)
//            Log.d("intercept", rect.centerX().toString() + " " + rect.centerY())
//            return rect.contains(ev.x.toInt(), ev.y.toInt())
//        }
//        return true;
//    }
}