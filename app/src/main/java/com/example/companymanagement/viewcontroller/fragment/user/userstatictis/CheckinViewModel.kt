package com.example.companymanagement.viewcontroller.fragment.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.checkin.CheckinModel
import com.example.companymanagement.model.checkin.CheckinRepository
import com.example.companymanagement.model.checkin.CheckingState
import com.example.companymanagement.model.performance.PerformanceModel
import com.example.companymanagement.model.performance.PerformanceRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.*

class CheckinViewModel : ViewModel() {
    var repo = CheckinRepository(FirebaseFirestore.getInstance().collection("checkin"));

    fun retriveLate(uuid: String, from: Date, end: Date): LiveData<MutableList<CheckinModel>> {
        var live: MutableLiveData<MutableList<CheckinModel>> = MutableLiveData()

        viewModelScope.launch {
            var data = repo.getChecking(uuid, CheckingState.late, from, end)
            live.postValue(data);
        }
        return live;
    }

    fun reTriveCheckinAll(
        uuid: String,
        from: Date,
        end: Date,
    ): LiveData<MutableList<CheckinModel>> {
        var live: MutableLiveData<MutableList<CheckinModel>> = MutableLiveData()
        viewModelScope.launch {
            var data = repo.getCheckingAll(uuid, from, end)
            live.postValue(data);
        }
        return live;
    }

    fun retriveOntime(uuid: String, from: Date, end: Date): LiveData<MutableList<CheckinModel>> {
        var live: MutableLiveData<MutableList<CheckinModel>> = MutableLiveData()
        viewModelScope.launch {
            var data = repo.getChecking(uuid, CheckingState.ontime, from, end)
            live.postValue(data);
        }
        return live;
    }

}
