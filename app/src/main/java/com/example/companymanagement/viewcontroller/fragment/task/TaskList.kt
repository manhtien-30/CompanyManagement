package com.example.companymanagement.viewcontroller.fragment.task

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
import com.example.companymanagement.viewcontroller.adapter.TaskRecyclerViewAdapter



class TaskList : Fragment() {
    private lateinit var taskViewModel: TaskViewModel
    private var adapter: TaskRecyclerViewAdapter = TaskRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel = ViewModelProvider(this.requireActivity()).get(TaskViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_tasklist)
        val layout = LinearLayoutManager(context)
        taskViewModel.TaskList.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
        layout.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sv = view.findViewById<SearchView>(R.id.search_tasklist)
        sv.queryHint = "Tìm kiếm Task theo Title"
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                taskViewModel.search(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                taskViewModel.search(newText!!)
                return true
            }
        })
    }
}