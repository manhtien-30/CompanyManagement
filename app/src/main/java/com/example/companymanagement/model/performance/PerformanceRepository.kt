package com.example.companymanagement.model.performance

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.companymanagement.model.ranking.RankerModel
import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class PerformanceRepository(var col: CollectionReference) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MMMM")

    suspend fun getLastDoc(uuid: String): PerformanceModel? {
        val snapshots =
            col.orderBy("create_time", Query.Direction.DESCENDING)
                .limit(1).whereEqualTo("owner_uuid", uuid).get().await()
        return if (snapshots.size() != 0)
            snapshots.documents[0].toObject(PerformanceModel::class.java)
        else
            null

    }

    suspend fun setDoc(performance: PerformanceModel) {
        col.add(performance).await()
    }

    suspend fun updateDoc(docid: String, performance: PerformanceModel) {
        val new = hashMapOf(
            "absence_a" to performance.AbsenceA,
            "absence_na" to performance.AbsenceNA,
            "late" to performance.Late,
            "task_done" to performance.TaskDone
        )
        col.document(docid).set(new, SetOptions.merge()).await()
    }

    //no use for now
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getDoc(uuid: String, month: YearMonth): PerformanceModel? {

        val ref = col.document(uuid)
            .collection(month.format(formatter)).document("performance_sumary")
        if (ref.get().await().exists())
            return ref.get().await().toObject(PerformanceModel::class.java)
        else {
            var dummy = PerformanceModel("N/A", 0, 0, 0, 0)
            dummy.uid = "month_sumary"
            return dummy
        }
    }

    suspend fun getDocByMonth(uuid: String, month: String, year: String): PerformanceModel? {

        val Cal = Calendar.getInstance()

        Cal.set(year.toInt(), month.toInt() - 1, 1, 0, 0, 0)
        val start = Cal.time

        Cal.set(year.toInt(), month.toInt(), 1, 0, 0, 0)
        val end = Cal.time


        val ref = col
            .whereEqualTo("owner_uuid", uuid)
            .whereGreaterThanOrEqualTo("create_time", start)
            .whereLessThan("create_time", end)
            .limit(1)
            .orderBy("create_time", Query.Direction.DESCENDING).get().await()

        Log.d("Performance", ref.size().toString())
        if (ref.size() == 0) {
            val dummy = PerformanceModel(uuid, 0, 0, 0, 0)
            dummy.CreateTime = start
            dummy.EndTime = end
            return dummy
        } else {
            return ref.documents[0].toObject(PerformanceModel::class.java)
        }
    }
}