package com.example.companymanagement.viewcontroller.fragment.task

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.employeemanage.EmployeeRepository
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.model.info.UserInfoRepository
import com.example.companymanagement.model.task.TaskInfoRepository
import com.example.companymanagement.model.task.UserTaskModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {
    var TaskList: MutableLiveData<MutableList<UserTaskModel>> = MutableLiveData()
    var repo = TaskInfoRepository(FirebaseFirestore.getInstance().collection("task"))
    var repo_employee = EmployeeRepository(FirebaseFirestore.getInstance().collection("userinfo"))
    var info: MutableLiveData<UserInfoModel> = MutableLiveData()
    val auth = FirebaseAuth.getInstance().currentUser;
    var repo_user = UserInfoRepository(FirebaseFirestore.getInstance().collection("userinfo"))
    init {
        viewModelScope.launch {
            TaskList.value = repo.getTask()
        }
        viewModelScope.launch {
            info.postValue(repo_user.findDoc(auth?.uid!!));
        }
    }
    fun search(string: String){
        viewModelScope.launch {
            TaskList.postValue(repo.getsearch(string))
        }
    }
    fun addTask(task: UserTaskModel) {
        viewModelScope.launch {
            val newdata = repo.addTask(task)
            if (newdata != null) {
                TaskList.value?.add(0, newdata)
                TaskList.postValue(TaskList.value)
            }
        }
    }
    fun updateTask(task: UserTaskModel){
        viewModelScope.launch {
            repo.updateTask(task);
        }
    }
    fun deleteTask(task: UserTaskModel){
        viewModelScope.launch {
            repo.deleteTask(task);
            TaskList.value?.remove(task)
            TaskList.postValue(TaskList.value)
        }
    }
    fun checkTask(str : String) :MutableLiveData<UserInfoModel> {
        var isvalid : MutableLiveData<UserInfoModel> = MutableLiveData()
        viewModelScope.launch{
            val result = repo_employee.checkEmail(str)
            Log.d("aaa1", result.toString())
            isvalid.value = result
            Log.d("aaa2", isvalid.value.toString())
        }
        return isvalid
    }
    fun count(str :String, month: String, year: String) : MutableLiveData<Int> {
        var result : MutableLiveData<Int> = MutableLiveData()
        viewModelScope.launch{
            val cnt = repo.countDone(str,month,year)
            Log.d("vbvb",cnt.toString())
            if (cnt != null) {
                result.value = cnt.size
            }
        }
        return result
    }


}