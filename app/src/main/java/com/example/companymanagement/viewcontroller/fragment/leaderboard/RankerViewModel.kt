package com.example.companymanagement.viewcontroller.fragment.leaderboard

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.performance.PerformanceModel
import com.example.companymanagement.model.ranking.RankerModel
import com.example.companymanagement.model.ranking.RankingRepository
import com.example.companymanagement.model.salary.SalaryRepository
import com.example.companymanagement.utils.VNeseDateConverter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
class RankerViewModel : ViewModel() {
    var rankList = MutableLiveData<List<RankerModel?>>()

    var ref = FirebaseFirestore.getInstance().collection("ranking")
    var repo = RankingRepository(ref)


    fun retrieveLeaderBoardIn(year : Int, month: Int) {
        viewModelScope.launch {
            rankList.postValue(repo.loadLeaderBoardIn(year, month))

        }
    }


}