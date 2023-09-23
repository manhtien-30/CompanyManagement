package com.example.companymanagement.viewcontroller.fragment.shareviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserRoleViewModel : ViewModel() {
    val isAdmin: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getRole(uuid: String) {
        viewModelScope.launch {
            try {
                var role =
                    FirebaseFirestore.getInstance().collection("userroles").document(uuid).get()
                        .await()
                        .getString("role")
                isAdmin.postValue(role == "admin");
            } catch (er: Exception) {
                isAdmin.postValue(false)
            }
        }
    }

}