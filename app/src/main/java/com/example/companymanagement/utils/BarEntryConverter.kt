package com.example.companymanagement.utils

import com.github.mikephil.charting.data.BarEntry

class BarEntryConverter {
    companion object {
        fun convert(x : Int, y: String) : BarEntry{
            return BarEntry(x + 0f, y.toFloat())
        }
    }
}