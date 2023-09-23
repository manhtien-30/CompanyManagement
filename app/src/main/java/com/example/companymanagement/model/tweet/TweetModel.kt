package com.example.companymanagement.model.tweet

import com.google.firebase.firestore.*
import java.util.*

@IgnoreExtraProperties
data class TweetModel(
    @get: PropertyName("content")
    @set: PropertyName("content")
    var Content: String = "",
    @get: PropertyName("owner_uuid")
    @set: PropertyName("owner_uuid")
    var OwnerUUID: String = "",
    @get: PropertyName("like_count")
    @set: PropertyName("like_count")
    var LikeCount: Int = 0,
) {
    @DocumentId
    val postuid: String? = null

    //  cái sẽ query trong nested col nên không cần query
    @get:Exclude
    @set:Exclude
    var comment: List<CommentModel>? = null
    //document id shall auto parse from doc by to object function . it should be unsetable

    @ServerTimestamp
    @get: PropertyName("create_time")
    @set: PropertyName("create_time")
    var CreateTime: Date? = null

    init {
        CreateTime = Date();
    }
}