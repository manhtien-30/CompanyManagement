package com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.leave.LeaveInfoModel
import com.example.companymanagement.model.leave.LeaveInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LeaveViewModel : ViewModel() {
    var LeaveList: MutableLiveData<MutableList<LeaveInfoModel>> = MutableLiveData()
    var rep = LeaveInfoRepository(FirebaseFirestore.getInstance().collection("leave"))
    val lazyloadsize: Long = 5;
    fun addleave(leave: LeaveInfoModel) {
        viewModelScope.launch {
            val newdata1 = rep.addDoc(leave)
            if (newdata1 != null) {
                LeaveList.value?.add(0, newdata1)
                LeaveList.postValue(LeaveList.value)
            }
        }
    }

    fun getleave(id: String) {
        viewModelScope.launch {

            val data = rep.getLeaveByOne(id, lazyloadsize)
            if (data != null) {
                LeaveList.postValue(data)
            }

        }
    }

    fun lazyLoadLeave(id: String): LiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            val newdata = rep.getLeaveByOne(id, lazyloadsize, LeaveList.value?.last()!!);
            if (newdata != null) {
                LeaveList.value?.addAll(newdata)
                result.postValue(newdata.size)
            }

        }
        return result;
    }

}