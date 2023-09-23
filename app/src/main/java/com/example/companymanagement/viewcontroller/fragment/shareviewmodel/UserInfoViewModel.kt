package com.example.companymanagement.viewcontroller.fragment.shareviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.model.info.UserInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserInfoViewModel : ViewModel() {
    var info: MutableLiveData<UserInfoModel> = MutableLiveData()
    var repo = UserInfoRepository(FirebaseFirestore.getInstance().collection("userinfo"))

    fun retriveUseInfo(uuid: String) {
        viewModelScope.launch {
            info.postValue(repo.findDoc(uuid));
        }
    }

    init {

        info.observeForever {
            viewModelScope.launch {
                repo.updateDoc(it);
            }
        }
    }

    fun getInfobyId(uuid: String) {
        viewModelScope.launch {
            info.postValue(repo.findDoc(uuid))
        }

    }
    fun updateInfo(userInfoModel: UserInfoModel){
        viewModelScope.launch {
            repo.updateDoc(userInfoModel);
        }
    }
}