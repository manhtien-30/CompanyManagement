package com.example.companymanagement.viewcontroller.fragment.manager.salary

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.databinding.FragmentLeaderboardBinding
import com.example.companymanagement.databinding.FragmentManagerSalaryBinding
import com.example.companymanagement.model.ranking.RankerModel
import com.example.companymanagement.model.salary.SalaryModel
import com.example.companymanagement.viewcontroller.adapter.LeaderBoardAdapter
import com.example.companymanagement.viewcontroller.adapter.ManagerSalaryAdapter
import com.example.companymanagement.viewcontroller.fragment.leaderboard.RankerViewModel
import com.example.companymanagement.viewcontroller.fragment.salary.SalaryViewModel
import com.google.android.material.appbar.MaterialToolbar
import java.time.YearMonth

class SalaryManager : Fragment()  {
    private var salaryList = arrayListOf<SalaryModel>()
    private var salaryListLayoutManager = LinearLayoutManager(activity)
    private lateinit var salaryListAdapter : ManagerSalaryAdapter

    private lateinit var viewModel: SalaryManagerViewModel

    private var _binding: FragmentManagerSalaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(SalaryManagerViewModel::class.java)
        _binding = FragmentManagerSalaryBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = view.findViewById<MaterialToolbar>(R.id.mnSalaryTopAppBar)
        val salaryListRecycleView = view.findViewById<RecyclerView>(R.id.salary_list_recycleview)

        var salaryListAdapter = ManagerSalaryAdapter()

        var newDialog = SearchAndFilterDialog()

        viewModel.getFullList(true, true)
        viewModel.searchResult.observe(viewLifecycleOwner, {
            salaryListAdapter.addSalaries(it as List<SalaryModel>)

            salaryListRecycleView.apply {
                layoutManager = salaryListLayoutManager
                adapter = salaryListAdapter
            }
        })

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    newDialog.show(childFragmentManager, "Filter")
                    // Handle search icon press
                    true
                }

                else -> false
            }
        }
    }
}