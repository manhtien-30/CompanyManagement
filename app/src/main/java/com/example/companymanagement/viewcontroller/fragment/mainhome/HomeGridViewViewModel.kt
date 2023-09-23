package com.example.companymanagement.viewcontroller.fragment.mainhome

import android.graphics.Color
import androidx.lifecycle.ViewModel

class HomeGridViewViewModel(
    val imageDrawable: Int,
    val linkID: Int?,
    val text: String,
    val backgroundColor: Color = Color(),
    val foregroundTint: Color? = null,
) {

}