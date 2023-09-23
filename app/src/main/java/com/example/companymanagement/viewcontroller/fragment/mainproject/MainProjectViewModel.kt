package com.example.companymanagement.viewcontroller.fragment.mainproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.task.UserTaskModel
import com.example.companymanagement.model.task.UserTaskRepository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.*

class MainProjectViewModel : ViewModel() {
    var taskList: MutableLiveData<MutableList<UserTaskModel>> = MutableLiveData()
    var repository = UserTaskRepository(FirebaseFirestore.getInstance().collection("task"))

    fun retrieveUserTask(
        uuid: String,
        start: Date, end: Date,
    ) {
        viewModelScope.launch {
            taskList.postValue(repository.getTask(uuid, start, end))

        }
    }

    fun updateStatus(task: UserTaskModel, status: String) {
        viewModelScope.launch {
            task.Status = status
            repository.updateTask(task);
        }
    }

}