package com.example.companymanagement.viewcontroller.fragment.performance

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.performance.PerformanceModel
import com.example.companymanagement.model.performance.PerformanceRepository
import com.example.companymanagement.utils.VNeseDateConverter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class PerformanceViewModel : ViewModel() {
    private var performRef = FirebaseFirestore.getInstance().collection("performance")
    private var performRepo = PerformanceRepository(performRef)


    fun updatePerformance(uuid: String, new : PerformanceModel) {
        // new - recent > 30 -> set new Performance
        // new - recent <= 30 -> update old performance
        val newDate = new.CreateTime!!

        viewModelScope.launch {
            val lastPer = performRepo.getLastDoc(uuid)
            Log.e("UID", lastPer?.uid.toString())
            Log.e("Date", lastPer?.CreateTime.toString())

            val now = Date()
            if(lastPer != null && now.before(lastPer.EndTime)){
                performRepo.updateDoc(lastPer.uid!!, new)
            }
            else{
                performRepo.setDoc(new)
            }
        }
    }

}