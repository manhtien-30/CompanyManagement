package com.example.companymanagement.model.tweet

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class CommentModel(
    @get: PropertyName("content")
    @set: PropertyName("content")
    var Content: String = "",

    @get: PropertyName("Poster_uuid")
    @set: PropertyName("Poster_uuid")
    var OwnerUUID: String = "",
) {
    @DocumentId
    val cmtuid: String? = null

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