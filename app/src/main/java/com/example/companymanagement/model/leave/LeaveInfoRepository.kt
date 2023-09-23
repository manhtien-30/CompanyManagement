package com.example.companymanagement.model.leave


import com.example.companymanagement.model.tweet.TweetModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.*

class LeaveInfoRepository(val col: CollectionReference) {


    suspend fun getLeaveResolved(count: Long = 10): MutableList<LeaveInfoModel>? {
        return col.whereIn("state", listOf("reject", "accept"))
            .orderBy("create_time", Query.Direction.DESCENDING)

            .limit(count).get().await()
            ?.toObjects(LeaveInfoModel::class.java)
    }

    suspend fun getLeaveResolved(
        count: Long = 10,
        startafter: LeaveInfoModel,
    ): MutableList<LeaveInfoModel>? {
        return col.whereIn("state", listOf("reject", "accept"))
            .orderBy("create_time", Query.Direction.DESCENDING).limit(count)
            .startAfter(startafter.CreateTime)
            .get().await().toObjects(LeaveInfoModel::class.java)
    }

    suspend fun getLeaveUnresolve(count: Long = 10): MutableList<LeaveInfoModel>? {
        return col.whereEqualTo("state", "undone")
            .orderBy("create_time", Query.Direction.DESCENDING)
            .limit(count).get().await()
            ?.toObjects(LeaveInfoModel::class.java)
    }

    suspend fun getLeaveUnresolve(
        count: Long = 10,
        startafter: LeaveInfoModel,
    ): MutableList<LeaveInfoModel>? {
        return col.whereEqualTo("state", "undone")
            .orderBy("create_time", Query.Direction.DESCENDING).limit(count)
            .startAfter(startafter.CreateTime)
            .get().await().toObjects(LeaveInfoModel::class.java)
    }

    suspend fun getLeaveByOne(id: String, limit: Long = 10): MutableList<LeaveInfoModel>? {

        return col.whereEqualTo("id_owner", id).orderBy("create_time", Query.Direction.DESCENDING)
            .limit(limit).get().await()?.toObjects(LeaveInfoModel::class.java)
    }

    suspend fun getLeaveByOne(
        id: String,
        count: Long = 10,
        startafter: LeaveInfoModel,
    ): MutableList<LeaveInfoModel>? {
        return col.whereEqualTo("id_owner", id)
            .orderBy("create_time", Query.Direction.DESCENDING).limit(count)
            .startAfter(startafter.CreateTime)
            .get().await().toObjects(LeaveInfoModel::class.java)
    }

    suspend fun addDoc(info: LeaveInfoModel): LeaveInfoModel? {
        return col.add(info).await()?.get()?.await()?.toObject(LeaveInfoModel::class.java)
    }

    suspend fun updateDoc(state: String, info: LeaveInfoModel) {
        info.UpdateTime = Date();
        info.check_Result = state
        col.document(info.luid!!).set(info).await()
    }
}