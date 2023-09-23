package com.example.companymanagement.model.checkin

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.*

enum class CheckingState {
    late,
    ontime
}

class CheckinRepository(var col: CollectionReference) {

    suspend fun getChecking(
        uuid: String,
        state: CheckingState,
        start: Date,
        end: Date,
    ): MutableList<CheckinModel> {

        var db = FirebaseFirestore.getInstance()
        return db.collectionGroup("checked_user")
            .whereGreaterThan("checked_date", start)
            .whereLessThan("checked_date", end)
            .whereEqualTo("owneruuid", uuid)
            .whereEqualTo("status", state.name).get().await().toObjects(CheckinModel::class.java)
    }

    suspend fun getCheckingAll(
        uuid: String,
        start: Date,
        end: Date,
    ): MutableList<CheckinModel> {
        var db = FirebaseFirestore.getInstance()
        return db.collectionGroup("checked_user")
            .whereGreaterThan("checked_date", start)
            .whereLessThan("checked_date", end)
            .whereEqualTo("owneruuid", uuid)
            .get().await().toObjects(CheckinModel::class.java)
    }
}