package com.example.companymanagement.model.tweet

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class CommentRepository(val col: CollectionReference) {

    suspend fun addComment(cmt: CommentModel): CommentModel? {
        return col.add(cmt).await().get().await().toObject(CommentModel::class.java)
    }

    suspend fun getListComment(count: Long = 10): MutableList<CommentModel>? {
        return col.orderBy("create_time", Query.Direction.DESCENDING).limit(count).get()
            .await()
            .toObjects(CommentModel::class.java)

    }

    suspend fun getListComment(
        count: Long = 10,
        startafter: CommentModel,
    ): MutableList<CommentModel>? {
        return col.orderBy("create_time", Query.Direction.DESCENDING).limit(count)
            .startAfter(startafter.CreateTime)
            .get().await().toObjects(CommentModel::class.java)
    }

    suspend fun deleteComment(commentuid: String) {
        col.document(commentuid).delete().await()
    }
}