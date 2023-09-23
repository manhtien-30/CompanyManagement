package com.example.companymanagement.utils

import android.content.Context
import android.util.DisplayMetrics

enum class Dimension {
    width, height
}

object UtilsFuntion {

    fun convertDPToPX(dp: Float, context: Context?): Float {
        return dp * (context?.resources?.displayMetrics?.density ?: 160F);
    }

    fun convertVHToPX(vh: Float, dimen: Dimension, context: Context?): Float {
        if (dimen == Dimension.width)
            return vh * (context?.resources?.displayMetrics?.widthPixels?.toFloat() ?: 1080F)
        else if (dimen == Dimension.height)
            return vh * (context?.resources?.displayMetrics?.heightPixels?.toFloat() ?: 1080F)
        return 0F;
    }
}