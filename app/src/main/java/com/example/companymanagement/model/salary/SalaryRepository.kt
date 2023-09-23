package com.example.companymanagement.model.salary

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap
import com.example.companymanagement.utils.VNeseDateConverter
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SalaryRepository (private var col: CollectionReference) {

    suspend fun getSalaryDoc(uuid: String, year : Int, month: Int) : SalaryModel? {
        val startCal = Calendar.getInstance()
        startCal.set(year, month - 1, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year, month, 1, 0, 0, 0)
        val end = endCal.time
/*        Log.e("start", start.toString())
        Log.e("end", end.toString())*/
        val ref = col.whereEqualTo("owner_uuid", uuid)
            .whereGreaterThanOrEqualTo("create_time", start)
            .whereLessThan("create_time", end).get().await()

        if(ref.size() == 0)
        {
            val dummy = SalaryModel(uuid, "dummy" ,0, 0, 0, 0, 0, 0, 0 )
            dummy.CreateTime = start
            dummy.EndTime = end
            return dummy
        }
        else {
            Log.e("docid", ref.documents[0].id)
            return ref.documents[0].toObject(SalaryModel::class.java)
        }
    }
    suspend fun getYearSalaryDocList(uuid: String, year: Int): List<SalaryModel?>{
        val startCal = Calendar.getInstance()
        startCal.set(year, 0, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year + 1, 0, 1, 0, 0, 0)
        val end = endCal.time

        val list = col.whereEqualTo("owner_uuid", uuid)
            .whereGreaterThanOrEqualTo("create_time", start)
            .whereLessThan("create_time", end)
            .get().await()
            .documents.map {
                it.toObject(SalaryModel::class.java)
            }
        var result = arrayListOf<SalaryModel>()
        for(index in 0 until 12){
            val dummy = generateDummy(year, index + 1)
            result.add(dummy)
            for(item in list){
                if (item != null) {
                    if(VNeseDateConverter.fromDateToMonth(item.CreateTime!!) == index + 1){
                        result[index] = item
                    }
                }
            }
        }
        return result
    }

    suspend fun getAllSalary(nameOrder : Boolean, timeOrder: Boolean): List<SalaryModel?> {
        if(nameOrder && timeOrder)
            return col
                .orderBy("owner_name", Query.Direction.ASCENDING)
                .orderBy("create_time", Query.Direction.ASCENDING)
                .get().await()
                .documents.map {
                    it.toObject(SalaryModel::class.java)
                }
        else {
            if (nameOrder && !timeOrder)
                return col
                    .orderBy("owner_name", Query.Direction.ASCENDING)
                    .orderBy("create_time", Query.Direction.DESCENDING)
                    .limit(12).get().await()
                    .documents.map {
                        it.toObject(SalaryModel::class.java)
                    }
            else {
                if (!nameOrder && timeOrder)
                    return col
                        .orderBy("owner_name", Query.Direction.DESCENDING)
                        .orderBy("create_time", Query.Direction.ASCENDING)
                        .limit(12).get().await()
                        .documents.map {
                            it.toObject(SalaryModel::class.java)
                        }
                else
                    return col
                        .orderBy("owner_name", Query.Direction.DESCENDING)
                        .orderBy("create_time", Query.Direction.DESCENDING)
                        .limit(12).get().await()
                        .documents.map {
                            it.toObject(SalaryModel::class.java)
                        }
            }
        }
    }

    suspend fun getAllSalaryByName(name : String, timeOrder : Boolean): List<SalaryModel?>{
        if(timeOrder)
            return col.whereEqualTo("owner_name", name)
                .orderBy("create_time", Query.Direction.ASCENDING)
                .get().await()
                .documents.map{
                    it.toObject(SalaryModel::class.java)
                }
        else
            return col.whereEqualTo("owner_name", name)
                .orderBy("create_time", Query.Direction.DESCENDING)
                .get().await()
                .documents.map{
                    it.toObject(SalaryModel::class.java)
                }
    }

    suspend fun getAllSalaryByNameAndYear(name : String, year: Int, timeOrder: Boolean) : List<SalaryModel?>{
        val startCal = Calendar.getInstance()
        startCal.set(year, 0, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year + 1, 0, 1, 0, 0, 0)
        val end = endCal.time
        if(timeOrder)
            return col.whereEqualTo("owner_name", name)
                .whereGreaterThanOrEqualTo("create_time", start)
                .whereLessThan("create_time", end)
                .orderBy("create_time", Query.Direction.ASCENDING)
                .get().await()
                .documents.map {
                    it.toObject(SalaryModel::class.java)
                }
        else
            return col.whereEqualTo("owner_name", name)
                .whereGreaterThanOrEqualTo("create_time", start)
                .whereLessThan("create_time", end)
                .orderBy("create_time", Query.Direction.DESCENDING)
                .get().await()
                .documents.map {
                    it.toObject(SalaryModel::class.java)
                }
    }

    suspend fun getAllSalaryByMonth(month : Int, year : Int, nameOrder: Boolean): List<SalaryModel?>{
        val startCal = Calendar.getInstance()
        startCal.set(year, month - 1, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year, month, 1, 0, 0, 0)
        val end = endCal.time
        if(nameOrder)
            return col.whereGreaterThanOrEqualTo("create_time", start)
                .whereLessThan("create_time", end)
                .orderBy("create_time")
                .orderBy("owner_name", Query.Direction.ASCENDING).get().await()
                .documents.map{
                    it.toObject(SalaryModel::class.java)
                }
        else
            return col.whereGreaterThanOrEqualTo("create_time", start)
                .whereLessThan("create_time", end)
                .orderBy("create_time")
                .orderBy("owner_name", Query.Direction.DESCENDING).get().await()
                .documents.map{
                    it.toObject(SalaryModel::class.java)
                }
    }



    suspend fun getAllSalaryByYear(year: Int, nameOrder: Boolean, timeOrder: Boolean) : List<SalaryModel?>{
        val startCal = Calendar.getInstance()
        startCal.set(year, 0, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year + 1, 0, 1, 0, 0, 0)
        val end = endCal.time

        if(nameOrder && timeOrder){
            return col
                .whereGreaterThanOrEqualTo("create_time", start)
                .whereLessThan("create_time", end)
                .orderBy("create_time", Query.Direction.ASCENDING)
                .orderBy("owner_name", Query.Direction.ASCENDING)
                .get().await()
                .documents.map {
                    it.toObject(SalaryModel::class.java)
                }
        }
        else{
            if(nameOrder && !timeOrder){
                return col
                    .whereGreaterThanOrEqualTo("create_time", start)
                    .whereLessThan("create_time", end)
                    .orderBy("create_time", Query.Direction.DESCENDING)
                    .orderBy("owner_name", Query.Direction.ASCENDING)
                    .get().await()
                    .documents.map {
                        it.toObject(SalaryModel::class.java)
                    }
            }
            else{
                if(!nameOrder && timeOrder){
                    return col
                        .whereGreaterThanOrEqualTo("create_time", start)
                        .whereLessThan("create_time", end)
                        .orderBy("create_time", Query.Direction.ASCENDING)
                        .orderBy("owner_name", Query.Direction.DESCENDING)
                        .get().await()
                        .documents.map {
                            it.toObject(SalaryModel::class.java)
                        }
                }
                else
                    return col
                        .whereGreaterThanOrEqualTo("create_time", start)
                        .whereLessThan("create_time", end)
                        .orderBy("create_time", Query.Direction.DESCENDING)
                        .orderBy("owner_name", Query.Direction.DESCENDING)
                        .get().await()
                        .documents.map {
                            it.toObject(SalaryModel::class.java)
                        }

            }
        }
    }

    suspend fun getAllSalaryByNameAndMonth(name: String, year: Int, month: Int): List<SalaryModel?>{
        val startCal = Calendar.getInstance()
        startCal.set(year, month - 1, 1, 0, 0, 0)
        val start = startCal.time
        val endCal = Calendar.getInstance()
        endCal.set(year, month, 1, 0, 0, 0)
        val end = endCal.time

        return col.whereEqualTo("owner_name", name)
            .whereGreaterThanOrEqualTo("create_time", start)
            .whereLessThan("create_time", end)
            .get().await()
            .documents.map{
                    it.toObject(SalaryModel::class.java)
            }
    }
    //
    private fun generateDummy(year : Int, month : Int) : SalaryModel {
        var dummy = SalaryModel()

        val start = Calendar.getInstance()
        start.set(year, month - 1, 1, 0, 0, 0)
        dummy.CreateTime = start.time
        val end = Calendar.getInstance()
        end.set(year, month, 1, 0, 0, 0)
        dummy.EndTime = end.time
        return dummy
    }

    //use for testing
    suspend fun getLastDoc(uuid : String) : SalaryModel? {
        val snapshots =
            col.orderBy("create_time", Query.Direction.DESCENDING)
                .limit(1).whereEqualTo("owner_uuid",uuid).get().await()
        return if(snapshots.size() != 0)
            snapshots.documents[0].toObject(SalaryModel::class.java)
        else
            null

    }
    suspend fun setDoc(salary: SalaryModel) {
        col.add(salary).await()
    }

    suspend fun updateDoc(docid: String, salary: SalaryModel){
        val new = hashMapOf(
            "rank_bonus" to salary.RankBonus,
            "checkin_fault_charge" to salary.CheckinFaultCharge,
            "task_bonus" to salary.TaskBonus

        )
        col.document(docid).set(new, SetOptions.merge()).await()
    }

}