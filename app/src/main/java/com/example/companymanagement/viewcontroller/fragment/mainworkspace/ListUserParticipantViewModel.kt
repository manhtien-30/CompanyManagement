package com.example.companymanagement.viewcontroller.fragment.mainworkspace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.model.info.UserInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ListUserParticipantViewModel : ViewModel() {
    var UserList: MutableLiveData<Hashtable<String, UserInfoModel>> = MutableLiveData();
    private var repo =
        UserInfoRepository(FirebaseFirestore.getInstance().collection("userinfo"))

    fun appendUser(uuid: String): LiveData<UserInfoModel?> {
        var User = MutableLiveData<UserInfoModel>()
        viewModelScope.launch(Dispatchers.Main) {
            var user = repo.findDoc(uuid)
            if (user != null) {
                UserList.value?.put(uuid, user!!);
                User.postValue(user!!);
            }
        }
        return User
    }

}