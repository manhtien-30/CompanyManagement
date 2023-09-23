package com.example.companymanagement.model.info

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import java.util.*

class UserInfoRepository(var col: CollectionReference) {
    // apply couroutine
    // it should be handle with try catch if a call is cancelation , for some purpose everyone also know, i throw them.But u guy should implement it
    suspend fun addDoc(uuid: String) {
        col.add(uuid).await();
    }

    suspend fun findDoc(uuid: String): UserInfoModel {
        var doc = col.document(uuid).get().await().toObject(UserInfoModel::class.java)
        if (doc == null) {
            col.document(uuid).set(UserInfoModel()).await()
            doc = col.document(uuid).get().await().toObject(UserInfoModel::class.java)!!
        }
        return doc
    }

    suspend fun updateDoc(info: UserInfoModel) {
        info.UpdateTime = Date();
        col.document(info.uid!!).set(info).await()
    }

    suspend fun deleteDoc(info: UserInfoModel) {
        col.document(info.uid!!).delete().await()
    }

    suspend fun getNameById(uuid: String): String {
        val foundUser = col.document(uuid).get().await()
            .toObject(UserInfoModel::class.java)
        return if (foundUser != null) {
            foundUser.Name!!
        } else
            "Invalid"
    }

    suspend fun getNameList(): ArrayList<String> {
        var result = arrayListOf<String>()
        val snapshots = col.get().await().documents.map {
            it.toObject(UserInfoModel::class.java)
        }
        for (item in snapshots) {
            if (item != null) {
                item.Name?.let { result.add(it) }
            }
        }
        return result
    }

    suspend fun getIdByName(name: String): MutableList<String> {
        val result = mutableListOf<String>()
        val snapshots = col.whereEqualTo("user_name", name).get().await().documents.map {
            it.toObject(UserInfoModel::class.java)
        }
        for (item in snapshots) {
            if (item != null) {
                item.uid?.let { result.add(it) }
            }
        }
        return result
    }
}