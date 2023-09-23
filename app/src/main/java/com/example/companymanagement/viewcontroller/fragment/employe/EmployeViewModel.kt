package com.example.companymanagement.viewcontroller.fragment.employe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.model.employeemanage.EmployeeRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class EmployeViewModel : ViewModel() {

    var EmployeeList: MutableLiveData<MutableList<UserInfoModel>> = MutableLiveData()
    var repo = EmployeeRepository(FirebaseFirestore.getInstance().collection("userinfo"))
    val newEmployee: MutableLiveData<UserInfoModel> = MutableLiveData()
    var repo_role = EmployeeRepository(FirebaseFirestore.getInstance().collection("userroles"))

    init {
        viewModelScope.launch {
            EmployeeList.value = repo.getEmployee()
        }
    }

    fun search(string: String) {
        viewModelScope.launch {
            EmployeeList.postValue(repo.getsearch(string))
        }
    }

    fun appendEmployee(uid: String) {
        viewModelScope.launch {
            val newdata = repo.getNewEmployee(uid)
            if (newdata != null) {
                EmployeeList.value?.add(0,newdata)
                EmployeeList.postValue(EmployeeList.value)
//                EmployeeList.value?.add(0,newdata)
            }
        }
    }
}