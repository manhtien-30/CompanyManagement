package com.example.companymanagement.model.salary

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.time.YearMonth

import androidx.annotation.Keep
import androidx.lifecycle.LifecycleOwner
import com.example.companymanagement.utils.VNeseDateConverter
import com.example.companymanagement.utils.VietnamDong
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.time.format.DateTimeFormatter
import java.util.*

@Keep
@RequiresApi(Build.VERSION_CODES.O)

data class SalaryModel (
    @get: PropertyName("owner_uuid")
    @set: PropertyName("owner_uuid")
    var OwnerUUID: String = "",
    @get: PropertyName("owner_name")
    @set: PropertyName("owner_name")
    var OwnerName: String = "",
    @get: PropertyName("basic_salary")
    @set: PropertyName("basic_salary")
    var BasicSalary: Long = 0,
    @get: PropertyName("rank_bonus")
    @set: PropertyName("rank_bonus")
    var RankBonus: Long = 0,
    @get: PropertyName("checkin_fault_charge")
    @set: PropertyName("checkin_fault_charge")
    var CheckinFaultCharge: Long = 0,
    @get: PropertyName("task_bonus")
    @set: PropertyName("task_bonus")
    var TaskBonus: Long = 0,
    @get: PropertyName("tax_deduction")
    @set: PropertyName("tax_deduction")
    var TaxDeduction: Long = 0,
    @get: PropertyName("total_bonus")
    @set: PropertyName("total_bonus")
    var TotalBonus: Long = 0,
    @get: PropertyName("total_salary")
    @set: PropertyName("total_salary")
    var TotalSalary: Long = 0,){

    @DocumentId
    var uid: String? = null

    @ServerTimestamp
    @get: PropertyName("create_time")
    @set: PropertyName("create_time")
    var CreateTime: Date? = null

    @ServerTimestamp
    @get: PropertyName("end_time")
    @set: PropertyName("end_time")
    var EndTime: Date? = null

    var expanded = false

    init {
        CreateTime = Date()
        EndTime = Date()
        val temp = VNeseDateConverter.fromDateToYearMonth(CreateTime!!)
        val cal = Calendar.getInstance()
        cal.set(temp.year, temp.month.value, 1, 0 ,0,0)
        EndTime!!.time = cal.timeInMillis
    }

    fun setBasicSalary(position : String) : VietnamDong {
        return if(position == "Manager")
            VietnamDong(BigDecimal(20000000))
        else
            VietnamDong(BigDecimal(10000000))
    }

    fun compute(month : YearMonth)
    {
        TaxDeduction = BasicSalary * month.lengthOfMonth() / 20
        TotalBonus = - CheckinFaultCharge + RankBonus - TaxDeduction

        TotalSalary = BasicSalary * month.lengthOfMonth() + TotalBonus
    }

    fun compute(year : String, month : String)
    {
        val datetime = VNeseDateConverter.convertStringToYearMonth(year, month)

        TaxDeduction = BasicSalary * datetime.lengthOfMonth() / 20
        TotalBonus = - CheckinFaultCharge + RankBonus - TaxDeduction

        TotalSalary = BasicSalary * datetime.lengthOfMonth() + TotalBonus
    }

}