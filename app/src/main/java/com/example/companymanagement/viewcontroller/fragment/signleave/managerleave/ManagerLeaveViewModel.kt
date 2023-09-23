package com.example.companymanagement.viewcontroller.fragment.signleave.employeLeave

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.leave.LeaveInfoModel
import com.example.companymanagement.model.leave.LeaveInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ManagerLeaveViewModel : ViewModel() {
    var LeaveList: MutableLiveData<MutableList<LeaveInfoModel>> = MutableLiveData()
    var rep = LeaveInfoRepository(FirebaseFirestore.getInstance().collection("leave"))
    private val lazyload: Long = 5
    fun getLeaveResolve() {
        viewModelScope.launch {
            LeaveList.postValue(rep.getLeaveResolved(lazyload))
        }
    }

    fun getLeaveUnresolve() {
        viewModelScope.launch {
            LeaveList.postValue(rep.getLeaveUnresolve(lazyload))
        }
    }

    fun lazyLoadUnresolve(): MutableLiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            val newdata = rep.getLeaveUnresolve(lazyload, LeaveList.value?.last()!!);
            if (newdata != null) {
                LeaveList.value?.addAll(newdata)
                result.postValue(newdata.size)
            }

        }
        return result;
    }

    fun lazyLoadResolve(): MutableLiveData<Int> {
        val result = MutableLiveData<Int>()
        viewModelScope.launch {
            val newdata = rep.getLeaveResolved(lazyload, LeaveList.value?.last()!!);
            if (newdata != null) {
                LeaveList.value?.addAll(newdata)
                result.postValue(newdata.size)
            }

        }
        return result;
    }

    fun accept(data: LeaveInfoModel) {
        viewModelScope.launch {
            rep.updateDoc("accept", data)
            LeaveList.value?.find { it.luid == data.luid }?.check_Result = "accept"
            LeaveList.postValue(LeaveList.value)

        }
    }

    fun reject(data: LeaveInfoModel) {
        viewModelScope.launch {
            rep.updateDoc("reject", data)
            LeaveList.value?.find { it.luid == data.luid }?.check_Result = "reject"
            LeaveList.postValue(LeaveList.value)
        }
    }


}