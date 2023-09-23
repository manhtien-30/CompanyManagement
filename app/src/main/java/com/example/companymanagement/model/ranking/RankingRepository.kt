package com.example.companymanagement.model.ranking

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.performance.PerformanceModel
import com.example.companymanagement.model.salary.SalaryModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class RankingRepository (var col: CollectionReference) {

    suspend fun loadLeaderBoardIn(year : Int, month : Int) : List<RankerModel?> {
        val startCal = Calendar.getInstance()
        startCal.set(year, month - 1, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year, month, 1, 0, 0, 0)
        val end = endCal.time

        val snapshots = col.whereGreaterThanOrEqualTo("create_time", start)
            .whereLessThan("create_time", end).orderBy("create_time")
            .orderBy("total_point", Query.Direction.DESCENDING).get().await()

        var list = listOf<RankerModel?>()

        if (snapshots != null) {
            list = snapshots.documents.map{
                it.toObject(RankerModel::class.java)
            }
        }
        return list
    }



}