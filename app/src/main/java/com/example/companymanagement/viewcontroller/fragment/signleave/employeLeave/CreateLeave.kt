package com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.model.leave.LeaveInfoModel
import com.example.companymanagement.utils.DateParser
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate
import com.example.companymanagement.viewcontroller.fragment.mainworkspace.ListUserParticipantViewModel
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import java.text.SimpleDateFormat
import java.util.*


class CreateLeave : Fragment() {
    private lateinit var leaveviewmodel: LeaveViewModel;
    private lateinit var userinfoviewmodel: UserInfoViewModel;
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        leaveviewmodel =
            ViewModelProvider(this.requireActivity()).get(LeaveViewModel::class.java)
        userinfoviewmodel =
            ViewModelProvider(this.requireActivity()).get(UserInfoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sign_leave, container, false)
        val editTime = root.findViewById<TextView>(R.id.txt_time)
        val cal = Calendar.getInstance()
        editTime!!.text = cal.getTime().toHumanReadDate()
        //Check Thoi gian ap dung
        val checkTodayTime = cal.time
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                editTime!!.text = cal.getTime().toHumanReadDate()
                val checkPickedTime = cal.time
                if (checkPickedTime.before(checkTodayTime) == true)
                    editTime.error = "Không hợp lệ!"
                else editTime.error = null
            }
        editTime.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()

            }
        }
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn_nop = view.findViewById<Button>(R.id.b_nopdon)
        val day_leave: EditText = view.findViewById(R.id.txt_day_leave)
        val editTime = view.findViewById<TextView>(R.id.txt_time)
        val reason: EditText = view.findViewById(R.id.editLido)
        btn_nop.setOnClickListener {
            if(editTime.error == null){
                try {
                    leaveviewmodel?.addleave(LeaveInfoModel(day_leave.text.toString().toInt(),
                        editTime.text.toString(),
                        reason.text.toString(),
                        userinfoviewmodel.info.value?.uid!!,
                        userinfoviewmodel.info.value?.Name.toString(),
                        "undone"))
                    day_leave.text.clear()
                    reason.text.clear()

                    Toast.makeText(context, "Nộp đơn thành công!", Toast.LENGTH_SHORT).show()
                } catch (err: Exception) {
                    toastEditfaild(err.message.toString())
                }
            } else {
                Toast.makeText(context, "Thời gian không hợp lệ, xin vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun toastEditfaild(err: String) {
        Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
    }
}

