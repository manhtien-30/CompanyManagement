package com.example.companymanagement.viewcontroller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.model.salary.SalaryModel
import com.example.companymanagement.utils.VNeseDateConverter
import com.example.companymanagement.utils.VietnamDong
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import java.math.BigDecimal

class ManagerSalaryAdapter : RecyclerView.Adapter<ManagerSalaryAdapter.SalaryViewHolder>() {

    private var salaries : MutableList<SalaryModel> = mutableListOf()
    override fun getItemCount(): Int = salaries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_salary, parent, false)

        return SalaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalaryViewHolder, position: Int) {
        val salary = salaries[position]
        var expanded = salary.expanded

        holder.name.text = salary.OwnerName

        holder.month.text = VNeseDateConverter.vnConvertMonth(salary.CreateTime!!)
        holder.year.text = VNeseDateConverter.fromDateToYear(salary.CreateTime!!).toString()

        holder.salary.text = VietnamDong(BigDecimal(salary.TotalSalary!!)).toString()

        holder.basic.text = VietnamDong(BigDecimal(salary.BasicSalary!!)).toString()
        holder.task.text = VietnamDong(BigDecimal(salary.TaskBonus!!)).toString()
        holder.rank.text = VietnamDong(BigDecimal(salary.RankBonus!!)).toString()
        holder.checkin.text = VietnamDong(BigDecimal(salary.CheckinFaultCharge!!)).toString()
        holder.tax.text = VietnamDong(BigDecimal(salary.TaxDeduction!!)).toString()
        holder.bonus.text = VietnamDong(BigDecimal(salary.TotalBonus!!)).toString()

        holder.subItem.visibility = if (expanded){
            View.VISIBLE
        } else{
            View.GONE
        }

        holder.itemView.setOnClickListener{
            var currentExpanded = salary.expanded
            salary.expanded = !currentExpanded
            notifyItemChanged(position)
        }

    }

    fun addSalaries(salaries: List<SalaryModel>) {
        this.salaries.apply {
            clear()
            addAll(salaries)
        }
        notifyDataSetChanged()
    }

    inner class SalaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.mn_salary_user_name)
        val year: TextView = itemView.findViewById(R.id.mn_salary_year)
        val month: TextView = itemView.findViewById(R.id.mn_salary_month)
        val salary: TextView = itemView.findViewById(R.id.mn_salary_amount)

        val basic: TextView = itemView.findViewById(R.id.mn_salary_basic)
        val task: TextView = itemView.findViewById(R.id.mn_salary_task)
        val rank: TextView = itemView.findViewById(R.id.mn_salary_rank)
        val checkin: TextView = itemView.findViewById(R.id.mn_salary_checkin)
        val tax: TextView = itemView.findViewById(R.id.mn_salary_tax)
        val bonus: TextView = itemView.findViewById(R.id.mn_salary_bonus)
        val subItem: LinearLayout = itemView.findViewById(R.id.sub_salary)
    }


}