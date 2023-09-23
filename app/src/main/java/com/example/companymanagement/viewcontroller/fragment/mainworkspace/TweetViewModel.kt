package com.example.companymanagement.viewcontroller.fragment.mainworkspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.tweet.TweetModel
import com.example.companymanagement.model.tweet.TweetRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class TweetViewModel : ViewModel() {
    var TweetList: MutableLiveData<MutableList<TweetModel>> = MutableLiveData();

    private var repo = TweetRepository(FirebaseFirestore.getInstance().collection("tweet"))


    init {
        viewModelScope.launch {
            TweetList.value = repo.getTweet(10)
        }
    }


    fun CountLikeUp(id: String) {
        viewModelScope.launch {
            repo.LikeCount(id)
        }
    }

    fun addTweet(tweet: TweetModel) {
        viewModelScope.launch {
            val newdata = repo.addNewTweet(tweet);
            if (newdata != null)
                TweetList.value?.add(0, newdata)
        }

    }

    fun lazyLoadTweet(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            val newdata = repo.getTweet(10, TweetList.value?.last()!!);
            if (newdata != null) {
                TweetList.value?.addAll(newdata)
                result.postValue(newdata.size)
            }

        }
        return result;
    }
}