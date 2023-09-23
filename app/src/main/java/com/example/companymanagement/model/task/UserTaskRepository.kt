package com.example.companymanagement.model.task

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.*

class UserTaskRepository(var col: CollectionReference) {
    // apply couroutine
    // it should be handle with try catch if a call is cancelation , for some purpose everyone also know, i throw them.But u guy should implement it

    suspend fun addNewTask(task: UserTaskModel): UserTaskModel? {
        return col.add(task).await().get().await().toObject(UserTaskModel::class.java)
    }

    suspend fun getTask(uuid: String): MutableList<UserTaskModel> {
        return col.whereArrayContains("IDReceiver", "$uuid")
            .get().await()
            .toObjects(UserTaskModel::class.java)
    }

    suspend fun updateTask(task: UserTaskModel) {
        col.document(task.taskid).set(task).await();
    }

    suspend fun getTask(
        uuid: String,
        start: Date,
        end: Date,
    ): MutableList<UserTaskModel> {
        val snapshot = col
            .whereArrayContains("IDReceiver", uuid)
            .whereGreaterThan("deadline", start)
            .whereLessThan("deadline", end)
            .orderBy("deadline", Query.Direction.DESCENDING)
            .get().await().toObjects(UserTaskModel::class.java)
        return snapshot
    }
}