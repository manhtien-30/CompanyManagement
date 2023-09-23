package com.example.companymanagement.viewcontroller.fragment.manager.salary

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.databinding.DialogFragmentMnSalarySearchFilterBinding
import com.example.companymanagement.model.ranking.RankerModel
import com.example.companymanagement.viewcontroller.adapter.NameFilterAdapter
import com.example.companymanagement.viewcontroller.adapter.NameListAdapter
import com.example.companymanagement.viewcontroller.fragment.salary.SalaryViewModel
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class SearchAndFilterDialog : DialogFragment(){
    private var _binding: DialogFragmentMnSalarySearchFilterBinding? = null
    private val binding get() = _binding!!

    lateinit var salaryViewModel : SalaryManagerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        salaryViewModel = ViewModelProvider(requireActivity()).get(SalaryManagerViewModel::class.java)

        _binding = DialogFragmentMnSalarySearchFilterBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.shape_conner_round)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val nameInput = view.findViewById<EditText>(R.id.name_enter)
        nameInput.setText("")
        val monthInput = view.findViewById<EditText>(R.id.month_enter)
        monthInput.setText(YearMonth.now().monthValue.toString())
        val yearInput = view.findViewById<EditText>(R.id.year_enter)
        yearInput.setText(YearMonth.now().year.toString())

        val timeOrder = view.findViewById<ToggleButton>(R.id.time_order)
        timeOrder.isChecked = true
        val nameOrder = view.findViewById<ToggleButton>(R.id.name_order)
        nameOrder.isChecked = true

        val topAppBar = view.findViewById<MaterialToolbar>(R.id.mnSalaryTopAppBar)
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.clear -> {
                    nameInput.setText("")
                    monthInput.setText("")
                    yearInput.setText("")

                    timeOrder.isChecked = true
                    nameOrder.isChecked = true
                    true
                }
                else -> false
            }
        }

        val yearBack = view.findViewById<ImageButton>(R.id.mn_salary_filter_year_back)
        val yearNext = view.findViewById<ImageButton>(R.id.mn_salary_filter_year_next)
        val monthBack = view.findViewById<ImageButton>(R.id.mn_salary_filter_month_back)
        val monthNext = view.findViewById<ImageButton>(R.id.mn_salary_filter_month_next)



        yearBack.setOnClickListener{
            if(yearInput.text.isNotBlank()){
                if(yearInput.text.toString().toInt() > YearMonth.now().year ){
                    yearInput.setText(YearMonth.now().year.toString())
                }
                if(yearInput.text.toString().toInt() > 2000 ){
                    yearInput.setText((yearInput.text.toString().toInt() - 1).toString())
                }
            }
        }
        yearNext.setOnClickListener{
            if(yearInput.text.isNotBlank() && yearInput.text.toString().toInt() < YearMonth.now().year ){
                yearInput.setText((yearInput.text.toString().toInt() + 1).toString())
            }
        }
        monthBack.setOnClickListener{
            if(monthInput.text.isNotBlank()){
                if(monthInput.text.toString().toInt() > 12){
                    monthInput.setText(12.toString())
                }
                if(monthInput.text.toString().toInt() > 1 ){
                    monthInput.setText((monthInput.text.toString().toInt() - 1).toString())
                }
            }
        }
        monthNext.setOnClickListener{
            if(monthInput.text.isNotBlank() && monthInput.text.toString().toInt() < 12 ){
                monthInput.setText((monthInput.text.toString().toInt() + 1).toString())
            }
        }

        val doneButton = view.findViewById<Button>(R.id.done_button)
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)

        doneButton.setOnClickListener {

            if(monthInput.text.isNotBlank() && yearInput.text.isBlank()){
                val builder = activity?.let { AlertDialog.Builder(it) }
                if (builder != null) {
                    builder.setTitle("Thời gian không rõ ràng ")
                    builder.setMessage("Hãy nhập năm chứa tháng")
                    builder.show()
                }
            }
            else{
                if(nameInput.text.isBlank() && yearInput.text.isBlank() && monthInput.text.isBlank()){
                    salaryViewModel.getFullList(nameOrder.isChecked, timeOrder.isChecked)
                }
                if(nameInput.text.isNotBlank() && yearInput.text.isBlank() && monthInput.text.isBlank()){
                    salaryViewModel.searchSalary(nameInput.text.toString(), timeOrder.isChecked)
                }
                if(nameInput.text.isNotBlank() && yearInput.text.isNotBlank() && monthInput.text.isBlank()) {
                    salaryViewModel.searchSalary(nameInput.text.toString(), yearInput.text.toString().toInt(), timeOrder.isChecked)
                }
                if(nameInput.text.isNotBlank() && yearInput.text.isNotBlank() && monthInput.text.isNotBlank()){
                    salaryViewModel.searchSalary(nameInput.text.toString(), yearInput.text.toString().toInt(), monthInput.text.toString().toInt())
                }
                if(nameInput.text.isBlank() && yearInput.text.isNotBlank() && monthInput.text.isNotBlank()){
                    salaryViewModel.searchSalary(monthInput.text.toString().toInt(), yearInput.text.toString().toInt(), nameOrder.isChecked)
                }
                if(nameInput.text.isBlank() && yearInput.text.isNotBlank() && monthInput.text.isBlank()){
                    salaryViewModel.searchSalary(yearInput.text.toString().toInt(), nameOrder.isChecked, timeOrder.isChecked)
                }
                dismiss()
            }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }




}