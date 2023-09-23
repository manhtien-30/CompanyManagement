package com.example.companymanagement.model.ranking

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.companymanagement.utils.VNeseDateConverter
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class RankerModel (
    @get: PropertyName("owner_uuid")
    @set: PropertyName("owner_uuid")
    var OwnerUUID: String = "",
    @get: PropertyName("total_point")
    @set: PropertyName("total_point")
    var TotalPoint : Long = 0,

        ){
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
        EndTime = Date()
        val temp = VNeseDateConverter.fromDateToYearMonth(CreateTime!!)
        val cal = Calendar.getInstance()
        cal.set(temp.year, temp.month.value, 1, 0 ,0,0)
        EndTime!!.time = cal.timeInMillis
    }


}