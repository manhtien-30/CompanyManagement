package com.example.companymanagement.viewcontroller.fragment.mainworkspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.tweet.CommentModel
import com.example.companymanagement.model.tweet.CommentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModel(idtweet: String) : ViewModel() {
    //xếp từ Mới đến củ
    val commentList: MutableLiveData<MutableList<CommentModel>> = MutableLiveData()
    val repo =
        CommentRepository(FirebaseFirestore.getInstance().collection("tweet").document(idtweet)
            .collection("comment"))
    val lastestQuerySize = MutableLiveData<Int>()

    init {
        viewModelScope.launch {
            var list = repo.getListComment(10);
            commentList.postValue(list);
        }
    }

    fun addComment(data: CommentModel): LiveData<Boolean> {
        var state = MutableLiveData(false)
        viewModelScope.launch(Dispatchers.Main) { //launch in main thread
            val newComment = repo.addComment(data)
            if (newComment != null) {
                commentList.value?.add(0, newComment!!)
                state.postValue(true);
            }
        }
        return state
    }

    fun loadOldComment() {

        viewModelScope.launch(Dispatchers.Main) { //launch in main thread
            if (commentList.value?.isEmpty() == false) {
                val newComment = repo.getListComment(10, commentList.value?.last()!!)
                if (newComment != null) {
                    commentList.value?.addAll(newComment)
                    lastestQuerySize.postValue(newComment.size)
                }

            }
            lastestQuerySize.postValue(0);
        }
    }
}