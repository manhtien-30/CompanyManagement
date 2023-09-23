package com.example.companymanagement.model.checkin

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class CheckinModel (
    @get: PropertyName("checked_date")
    @set: PropertyName("checked_date")
    var checked_date: Date = Date(),

    @get: PropertyName("status")
    @set: PropertyName("status")
    var status: String? = null,

    @get: PropertyName("owneruuid")
    @set: PropertyName("owneruuid")
    var owneruuid: String? = null
){
    @DocumentId
    var check_id: String = ""
    //document id shall auto parse from doc by to object function . it should be unsetable

}