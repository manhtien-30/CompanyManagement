package com.example.companymanagement.viewcontroller.fragment.user.task

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.companymanagement.R
import org.jetbrains.annotations.NotNull

class Task_Dialog : AppCompatDialogFragment() {
    @NotNull
    @Override
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var inflater: LayoutInflater? = activity?.layoutInflater
        var root: View? = inflater?.inflate(R.layout.fragment_data_project,null)

        builder.setView(root)
            .setPositiveButton("Oke"
            ) { dialog, which -> }
        return builder.create()
    }
}