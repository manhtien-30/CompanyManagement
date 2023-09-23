package com.example.companymanagement.model.performance

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.companymanagement.utils.VNeseDateConverter
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.time.YearMonth
import java.util.*

class PerformanceModel (
    @get: PropertyName("owner_uuid")
    @set: PropertyName("owner_uuid")
    var OwnerUUID: String = "",
    @get: PropertyName("absence_a")
    @set: PropertyName("absence_a")
    var AbsenceA: Long = 0,
    @get: PropertyName("absence_na")
    @set: PropertyName("absence_na")
    var AbsenceNA: Long = 0,
    @get: PropertyName("late")
    @set: PropertyName("late")
    var Late: Long = 0,
    @get: PropertyName("task_done")
    @set: PropertyName("task_done")
    var TaskDone: Long = 0 ) {

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

    init {
        CreateTime = Date()
        //Log.e("CreateTime", CreateTime.toString())
        EndTime = Date()
        val temp = VNeseDateConverter.fromDateToYearMonth(CreateTime!!)
        val cal = Calendar.getInstance()
        cal.set(temp.year, temp.month.value + 1, 1, 0 ,0,0)
        EndTime!!.time = cal.timeInMillis
        //Log.e("EndTime", EndTime.toString())
    }

    fun setAbsenceAndLate (uid : String, month : String/*, list : AbsenceListModel*/) {
        //ToDo : retry absence list of chosen emp in certain month from Checking Collection
        //ToDo : count absence type A and NA and late
    }
    fun setTaskDone (uid : String, month : String/*, list : TaskListModel*/){
        //ToDo : retry task list of chosen emp in certain month from Task Collection
        //ToDo : count task with completed status
    }


    // no use for now
    fun computeTotalPoint() = TaskDone * 2 - Late * 2 - AbsenceA - AbsenceNA * 3
    fun computeTaskBonus() = 50000 * TaskDone
    fun computeLateCharge() : Long {
        return if (Late > 1)
             (Late - 1) * 50000
        else
            0
    }
    fun computeAbsenceACharge() : Long {
        return if(AbsenceA > 3)
            (AbsenceA - 3) * 50000
        else
            0
    }
    fun computeAbsenceNACharge() = AbsenceNA * 100000
    fun computeCheckinFaultCharge() =  computeLateCharge() + computeAbsenceACharge() + computeAbsenceNACharge()

    fun getRank(){
        //developing
    }
    fun computeRankBonus(){
        //developing
    }

}