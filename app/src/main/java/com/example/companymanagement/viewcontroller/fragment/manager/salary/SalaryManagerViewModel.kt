package com.example.companymanagement.viewcontroller.fragment.manager.salary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companymanagement.model.salary.SalaryModel
import com.example.companymanagement.model.salary.SalaryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class SalaryManagerViewModel : ViewModel() {
    private var salaryRef = FirebaseFirestore.getInstance().collection("salary")
    private var salaryRepo = SalaryRepository(salaryRef)


    var searchResult = MutableLiveData<List<SalaryModel?>>()

    fun getFullList(nameOrder : Boolean, timeOrder: Boolean){
        viewModelScope.launch {
            searchResult.postValue(salaryRepo.getAllSalary(nameOrder, timeOrder))
        }
    }
    fun searchSalary(searchName: String, timeOrder: Boolean){
        viewModelScope.launch {
            searchResult.postValue(salaryRepo.getAllSalaryByName(searchName, timeOrder))
        }
    }
    fun searchSalary(searchName: String, searchYear: Int, timeOrder: Boolean){
        viewModelScope.launch {
            searchResult.postValue(salaryRepo.getAllSalaryByNameAndYear(searchName, searchYear, timeOrder))
        }
    }
    fun searchSalary(searchMonth: Int, searchYear: Int, nameOrder: Boolean){
        viewModelScope.launch {
            searchResult.postValue(salaryRepo.getAllSalaryByMonth(searchMonth, searchYear, nameOrder))
        }
    }
    fun searchSalary(searchYear: Int, nameOrder: Boolean, timeOrder: Boolean){
        viewModelScope.launch {
            searchResult.postValue(salaryRepo.getAllSalaryByYear(searchYear, nameOrder, timeOrder))
        }
    }
    fun searchSalary(searchName : String, searchYear: Int, searchMonth: Int){
        viewModelScope.launch {
            searchResult.postValue(salaryRepo.getAllSalaryByNameAndMonth(searchName, searchYear, searchMonth))
        }
    }



}