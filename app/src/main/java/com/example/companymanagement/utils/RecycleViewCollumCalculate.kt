package com.example.companymanagement.utils

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecycleViewCalculate(context: Context, viewId: Int) {
    private val width: Int
    private val height: Int
    private var remaining = 0
    private val displayMetrics: DisplayMetrics
    fun calculateNoOfColumns(): Int {
        var numberOfColumns = displayMetrics.widthPixels / width
        remaining = displayMetrics.widthPixels - numberOfColumns * width
        //        System.out.println("\nRemaining\t" + remaining + "\nNumber Of Columns\t" + numberOfColumns);
        if (remaining / (2 * numberOfColumns) < 15) {
            numberOfColumns--
            remaining = displayMetrics.widthPixels - numberOfColumns * width
        }
        return numberOfColumns
    }

    fun calculateSpacing(): Int {
        val numberOfColumns = calculateNoOfColumns()
        //        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);
        return remaining / (2 * numberOfColumns)
    }

    init {
        val view: View = View.inflate(context, viewId, null)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        width = view.getMeasuredWidth()
        height = view.getMeasuredHeight()
        displayMetrics = context.getResources().getDisplayMetrics()
    }

    class SpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            outRect.left = spacing
            outRect.right = spacing
            outRect.bottom = spacing
            outRect.top = spacing
        }
    }
}
