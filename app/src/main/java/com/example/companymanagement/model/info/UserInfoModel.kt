package com.example.companymanagement.model.info

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@Keep
data class UserInfoModel(
    @get: PropertyName("user_name")
    @set: PropertyName("user_name")
    var Name: String? = null,
    @get: PropertyName("contact_email")
    @set: PropertyName("contact_email")
    var Email: String? = null,
    @get: PropertyName("phone_number")
    @set: PropertyName("phone_number")
    var PhoneNumber: String? = null,

    @get: PropertyName("avatar_url")
    @set: PropertyName("avatar_url")
    var AvatarURL: String? = null,

    @get: PropertyName("position")
    @set: PropertyName("position")
    var Position: String? = null,

    @get: PropertyName("birth_date")
    @set: PropertyName("birth_date")
    var BirthDate: Date? = null,

    @get: PropertyName("gender")
    @set: PropertyName("gender")
    var Gender: String? = null,


    @get: PropertyName("id_card_number")
    @set: PropertyName("id_card_number")
    var IDCardNumber: String? = null,

    @get: PropertyName("idcard_create_date")
    @set: PropertyName("idcard_create_date")
    var IDCardCreateDate: Date? = null,

    @get: PropertyName("idcard_create_location")
    @set: PropertyName("idcard_create_location")
    var IDCardCreateLocation: String? = null,
) {
    @DocumentId
    val uid: String? = null

    //document id shall auto parse from doc by to object function . it should be unsetable

    @ServerTimestamp
    @get: PropertyName("create_time")
    @set: PropertyName("create_time")
    var CreateTime: Date? = null

    @ServerTimestamp
    @get: PropertyName("update_time")
    @set: PropertyName("update_time")
    var UpdateTime: Date? = null

    init {
        CreateTime = Date();
        UpdateTime = Date();
    }

}