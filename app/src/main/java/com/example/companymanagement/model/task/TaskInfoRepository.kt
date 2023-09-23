package com.example.companymanagement.model.task

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.*

class TaskInfoRepository(var col: CollectionReference) {
    suspend fun addTask(task: UserTaskModel): UserTaskModel?{
        return col.add(task).await().get().await().toObject(UserTaskModel::class.java)
    }
    suspend fun getNewTask(uid : String): UserTaskModel?{
        return col.document(uid).get().await().toObject(UserTaskModel::class.java)
    }
    suspend fun getTask(): MutableList<UserTaskModel>?  {
        return col.orderBy("sentDate",Query.Direction.DESCENDING).get()
            .await().toObjects(UserTaskModel::class.java)
    }
    suspend fun updateTask(task : UserTaskModel) {
        col.document(task.taskid!!).set(task).await()
    }
    suspend fun deleteTask(task : UserTaskModel) {
        col.document(task.taskid!!).delete().await()
    }
    suspend fun getsearch(text: String): MutableList<UserTaskModel>? {
//        return col.whereEqualTo("user_name",text).orderBy("create_time", Query.Direction.DESCENDING).get()
        return col.whereGreaterThanOrEqualTo("title",text).get()
            .await()
            .toObjects(UserTaskModel::class.java)
    }
    suspend fun countDone(text : String, month: String, year: String): List<UserTaskModel?> {
        val Cal = Calendar.getInstance()

        Cal.set(year.toInt(), month.toInt() - 1, 1,0,0,0)
        val start = Cal.time

        Cal.set(year.toInt(), month.toInt(), 1,0,0,0)
        val end = Cal.time
        val ref = col.whereEqualTo("status",text).whereGreaterThanOrEqualTo("deadline",start)
            .whereLessThan("deadline",end)
            .get().await().documents.map {
                it.toObject(UserTaskModel::class.java)
            }
        Log.d("time1",start.toString())
        Log.d("time2",end.toString())
        return ref
    }
}
