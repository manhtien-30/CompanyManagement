package com.example.companymanagement.viewcontroller.fragment.employe

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.utils.DateParser
import com.example.companymanagement.utils.DateParser.Companion.toHumanReadDate
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import java.io.FileDescriptor
import java.io.PrintWriter

class Employe_ItemShow( var userInfo: UserInfoModel) : DialogFragment() {

    private var infomodel: UserInfoViewModel = UserInfoViewModel()
    private  lateinit var employeViewModel: EmployeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeViewModel = ViewModelProvider(this.requireActivity()).get(EmployeViewModel::class.java)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_dialog_employee, LinearLayout(activity), false)

        val dlg = Dialog(view.context)
        dlg.setContentView(view)
        val dlBirthDate = dlg.findViewById<EditText>(R.id.dl_employee_birthdate)
        val dlCreateTime = dlg.findViewById<EditText>(R.id.dl_employee_createdate)
        val dlGender = dlg.findViewById<EditText>(R.id.dl_employee_gender)
        val dlIdCard = dlg.findViewById<EditText>(R.id.dl_employee_idcard)
        val dlIdCardDate = dlg.findViewById<EditText>(R.id.dl_employee_idcardcd)
        val dlIdCardLocation = dlg.findViewById<EditText>(R.id.dl_employee_idcardloc)
        val dlPhone = dlg.findViewById<EditText>(R.id.dl_employee_phone)
        val dlPosition = dlg.findViewById<EditText>(R.id.dl_employee_pos)
        val dlUpdateTime = dlg.findViewById<EditText>(R.id.dl_employee_updatetime)
        val dlEmail = dlg.findViewById<EditText>(R.id.dl_employee_contactemail)
        val dlfName = dlg.findViewById<EditText>(R.id.dl_employee_username)
        val edit = dlg.findViewById<ImageView>(R.id.edit)

        dlGender.setText(userInfo.Gender)
        dlBirthDate.setText(userInfo.BirthDate?.toHumanReadDate())
        dlIdCard.setText(userInfo.IDCardNumber)
        dlIdCardLocation.setText(userInfo.IDCardCreateLocation)
        dlPhone.setText(userInfo.PhoneNumber)
        dlPosition.setText(userInfo.Position)
        dlEmail.setText(userInfo.Email)
        dlfName.setText(userInfo.Name)
        dlCreateTime.setText(userInfo.CreateTime?.toHumanReadDate())
        dlUpdateTime.setText(userInfo.UpdateTime?.toHumanReadDate())
        dlIdCardDate.setText(userInfo.IDCardCreateDate?.toHumanReadDate())


        edit.setOnClickListener {
            if (edit.tag == "cant_change") {
                Toast.makeText(context, "Cho phép chỉnh sửa thông tin", Toast.LENGTH_SHORT)
                    .show()
                edit.tag = "can_change"
                edit.setImageResource(R.drawable.ic_check)
                dlPosition.isEnabled = true
                dlGender.isEnabled = true
                dlBirthDate.isEnabled = true
                dlIdCard.isEnabled = true
                dlIdCardLocation.isEnabled = true
                dlIdCardDate.isEnabled = true
                dlPhone.isEnabled = true
                dlEmail.isEnabled = true
                dlfName.isEnabled = true
            } else if (edit.tag == "can_change") {
                edit.tag = "cant_change"
                edit.setImageResource(R.drawable.ic_edit)
                dlPosition.isEnabled = false
                dlGender.isEnabled = false
                dlBirthDate.isEnabled = false
                dlIdCard.isEnabled = false
                dlIdCardLocation.isEnabled = false
                dlIdCardDate.isEnabled = false
                dlPhone.isEnabled = false
                dlEmail.isEnabled = false
                dlfName.isEnabled = false
                try {
                    var user = userInfo.apply {
                        Name = dlfName.text.toString()
                        PhoneNumber = dlPhone.text.toString()
                        Email = dlEmail.text.toString()
                        Position = dlPosition.text.toString()
                        BirthDate = DateParser.parser(dlBirthDate.text.toString())
                        Gender = dlGender.text.toString()
                        IDCardNumber = dlIdCard.text.toString()
                        IDCardCreateDate = DateParser.parser(dlIdCardDate.text.toString())
                        IDCardCreateLocation = dlIdCardLocation.text.toString()
                    }
                    infomodel.updateInfo(user)

                    var t = employeViewModel.EmployeeList.value?.find {
                        it.uid == user.uid
                    }.apply {
                        this?.Name = user.Name
                        this?.CreateTime = user.CreateTime
                        this?.UpdateTime = user.UpdateTime
                        this?.IDCardCreateLocation = user.IDCardCreateLocation
                        this?.IDCardCreateDate = user.IDCardCreateDate
                        this?.IDCardNumber = user.IDCardNumber
                        this?.Gender = user.Gender
                        this?.Email = user.Email
                        this?.PhoneNumber = user.PhoneNumber
                        this?.BirthDate = user.BirthDate
                        this?.Position = user.Position
                    }
                    employeViewModel.EmployeeList.postValue(
                        employeViewModel.EmployeeList.value
                    )
                    Toast.makeText(context, "Hoàn tất chỉnh sửa thông tin", Toast.LENGTH_LONG)
                        .show()
                } catch (ex: Exception) {
                    Toast.makeText(context, ex.message!!, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        return dlg
    }

}
