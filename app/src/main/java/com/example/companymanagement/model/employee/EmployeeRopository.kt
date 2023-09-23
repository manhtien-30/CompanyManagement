package com.example.companymanagement.model.employeemanage

import com.example.companymanagement.model.info.UserInfoModel
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

class EmployeeRepository(val col: CollectionReference)  {
    suspend fun addNewEmployee(employee: UserInfoModel): UserInfoModel?{
        return col.add(employee).await().get().await().toObject(UserInfoModel::class.java)
    }
    suspend fun getNewEmployee(uid : String): UserInfoModel?{
        return col.document(uid).get().await().toObject(UserInfoModel::class.java)
    }
    suspend fun getEmployee(): MutableList<UserInfoModel>?  {
        return col.orderBy("create_time", Query.Direction.DESCENDING).get()
            .await().toObjects(UserInfoModel::class.java)
    }
    suspend fun getsearch(text: String): MutableList<UserInfoModel>? {
//        return col.whereEqualTo("user_name",text).orderBy("create_time", Query.Direction.DESCENDING).get()
        return col.whereGreaterThanOrEqualTo("user_name",text).get()
            .await()
            .toObjects(UserInfoModel::class.java)
    }
    suspend fun checkEmail(text : String) : UserInfoModel? {
        val check = col.whereEqualTo("contact_email",text).get().await().toObjects(UserInfoModel::class.java)
        if(check.size > 0)
            return check[0]
        return null
    }
}