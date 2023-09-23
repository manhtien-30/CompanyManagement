package com.example.companymanagement.viewcontroller.fragment.employe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.EmployeeRecyclerViewAdapter



class EmployeList : Fragment() {

    private lateinit var employeViewModel: EmployeViewModel
    private var adapter: EmployeeRecyclerViewAdapter = EmployeeRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        employeViewModel =  ViewModelProvider(this.requireActivity()).get(EmployeViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_employe_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_employeelist)
        val layout = LinearLayoutManager(context)

        employeViewModel.EmployeeList.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        layout.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

//        employeViewModel.newEmployee.observe(viewLifecycleOwner) {
//            adapter.setNewData(it)
//        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sv = view.findViewById<SearchView>(R.id.search_employeelist)


        sv.queryHint = "Tìm kiếm nhân viên theo họ tên"
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                employeViewModel.search(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                employeViewModel.search(newText!!)
                return true
            }
        })
//        EmployeViewModel.EmployeeList.observe(viewLifecycleOwner) {
//            adapter.setData(it)
//        }


    }

}

