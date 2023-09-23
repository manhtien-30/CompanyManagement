package com.example.companymanagement.viewcontroller.fragment.salary

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.companymanagement.R
import com.example.companymanagement.utils.BarEntryConverter
import com.example.companymanagement.utils.VNeseDateConverter
import com.example.companymanagement.utils.VietnamDong
import com.example.companymanagement.viewcontroller.fragment.performance.PerformanceViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.auth.FirebaseAuth
import java.math.BigDecimal
import java.time.Year
import java.time.YearMonth



class SalaryFragment : Fragment() {

    var auth = FirebaseAuth.getInstance()
    val uuid = auth.currentUser?.uid!!.toString()

    //thing needed for salary detail\
    lateinit var performanceViewModel: PerformanceViewModel
    lateinit var salaryViewModel : SalaryViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        salaryViewModel = ViewModelProvider(requireActivity()).get(SalaryViewModel::class.java)
        performanceViewModel = ViewModelProvider(requireActivity()).get(PerformanceViewModel::class.java)


        return inflater.inflate(R.layout.fragment_salary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val salaryChart = view.findViewById<BarChart>(R.id.salary_chart)


        val yearNextButton = view.findViewById<ImageButton>(R.id.leaderboard_button_year_next)
        val yearBackButton = view.findViewById<ImageButton>(R.id.leaderboard_button_year_back)
        val yearDisplay = view.findViewById<TextView>(R.id.leaderboard_display_time)

        yearNextButton.setOnClickListener {
            val temp = yearDisplay.text.toString().toInt()
            if(temp < Year.now().toString().toInt()) {
                yearDisplay.text = (temp + 1).toString()
            }
        }
        yearBackButton.setOnClickListener {
            val temp = yearDisplay.text.toString().toInt()
            yearDisplay.text = (temp - 1).toString()
        }
        //Show Salary Chart of a chosen Year
        yearDisplay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                salaryViewModel.retrieveSalary(uuid, yearDisplay.text.toString().toInt(), YearMonth.now().monthValue)
                salaryViewModel.retrieveYearlySalary(uuid, yearDisplay.text.toString().toInt())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        yearDisplay.text = Year.now().toString()


        val salaryTime = view.findViewById<TextView>(R.id.salary_time)
        val basicSalary = view.findViewById<TextView>(R.id.salary_basic)
        val checkinFaultCharge = view.findViewById<TextView>(R.id.salary_checkin_fault)
        val taskBonus = view.findViewById<TextView>(R.id.salary_task_bonus)
        val rankBonus = view.findViewById<TextView>(R.id.salary_rank_bonus)
        val taxDeduction = view.findViewById<TextView>(R.id.salary_tax_deduction)
        val totalBonus = view.findViewById<TextView>(R.id.salary_total_bonus)
        val totalSalary = view.findViewById<TextView>(R.id.salary_total)

        salaryViewModel.salary.observe(viewLifecycleOwner, {
            salaryTime.text = VNeseDateConverter.vnConvertMonth(it.CreateTime!!)

            basicSalary.text = VietnamDong(BigDecimal(it.BasicSalary)).toString()
            checkinFaultCharge.text = VietnamDong(BigDecimal(it.CheckinFaultCharge)).toString()
            taskBonus.text = VietnamDong(BigDecimal(it.TaskBonus)).toString()
            rankBonus.text = VietnamDong(BigDecimal(it.RankBonus)).toString()
            taxDeduction.text = VietnamDong(BigDecimal(it.TaxDeduction)).toString()
            totalBonus.text = VietnamDong(BigDecimal(it.TotalBonus)).toString()
            totalSalary.text = VietnamDong(BigDecimal(it.TotalSalary)).toString()
        })

        salaryViewModel.salaryList
            .observe(viewLifecycleOwner, {
            val list = arrayListOf<BarEntry>()
            for(i in 0 until 12){
                list.add(BarEntryConverter.convert(i, (it[i]!!.TotalSalary / 1000).toString()))
            }
            val nowBarData = BarDataSet(list, "Salary (K VND").apply {
                //valueTextColor = Color.BLACK
                color = ContextCompat.getColor(requireContext(), R.color.purple_200)
                //valueTextSize = 10f
                barBorderWidth = 0.5f

                setDrawValues(false)
            }


            salaryChart.data = BarData(nowBarData)
            //set some attributes for the chart
            salaryChart.animateY(1000) //set the animation for the yAxis (raise up in 0.1 second)
            salaryChart.xAxis.valueFormatter = AxisFormater() //format the xAxis to string with a class
            salaryChart.xAxis.textSize = 12f //set text size of xAxis label
            salaryChart.xAxis.textColor = Color.RED //set text color
            salaryChart.xAxis.labelRotationAngle = 35f //set rotation for xAxis label
            salaryChart.xAxis.position = XAxis.XAxisPosition.BOTTOM //Move xAxis label to bottom
            salaryChart.description = null //turn off the useless label description
            salaryChart.setScaleEnabled(false) //disable scalability
            salaryChart.axisLeft.axisMinimum = 0f
            salaryChart.axisRight.axisMinimum = 0f
            salaryChart.extraBottomOffset = 35f // add space from x-Axis
            salaryChart.invalidate()
        })


        salaryChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onNothingSelected() {
                //when nothing is selected
                //no idea yet
            }
            //tap on a bar to show detail of that month's salary
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    salaryViewModel.showChosenMonthSalary(e.x.toInt())
                }
                salaryChart.highlightValue(h)
            }
        })
    }


    //This class is used to reformat the xAxis label from float to string
    class AxisFormater : ValueFormatter() {
        private val months = arrayOf("Jan", "Feb", "Mar",
            "Apr", "May", "June", "July", "Aug",
            "Sep", "Oct", "Nov", "Dec")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return months.getOrNull(value.toInt()) ?: value.toString()
        }
    }

}