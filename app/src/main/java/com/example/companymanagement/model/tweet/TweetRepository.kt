package com.example.companymanagement.model.tweet

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class TweetRepository(val col: CollectionReference) {
    suspend fun addNewTweet(tweet: TweetModel): TweetModel? {
        return col.add(tweet).await().get().await().toObject(TweetModel::class.java)
    }

    suspend fun getTweet(count: Long = 10): MutableList<TweetModel>? {
        return col.orderBy("create_time", Query.Direction.DESCENDING).limit(count).get().await()
            .toObjects(TweetModel::class.java)
    }

    suspend fun LikeCount(id: String) {

        col.document(id).update("like_count", FieldValue.increment(1)).await();
    }

    suspend fun getTweet(count: Long = 10, startafter: TweetModel): MutableList<TweetModel>? {
        return col.orderBy("create_time", Query.Direction.DESCENDING).limit(count)
            .startAfter(startafter.CreateTime)
            .get().await().toObjects(TweetModel::class.java)
    }
}