package com.example.companymanagement.viewcontroller.fragment.user.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.adapter.UserTaskAdapter
import com.example.companymanagement.viewcontroller.fragment.user.UserTaskViewModel


class UserTask : Fragment() {

    private var taskModel: UserTaskViewModel = UserTaskViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        taskModel = ViewModelProvider(this).get(UserTaskViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_project, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userTaskAdapter = UserTaskAdapter()
        val layoutmanager = LinearLayoutManager(context)
        val taskRecyclerView: RecyclerView = view.findViewById(R.id.user_task_container)
        taskRecyclerView.layoutManager = layoutmanager
        layoutmanager.orientation = RecyclerView.VERTICAL
        taskRecyclerView.adapter = userTaskAdapter

        taskModel.TaskList.observe(this.viewLifecycleOwner) {
            userTaskAdapter.setData(it)
        }
        taskModel.TaskList.observe(this.viewLifecycleOwner) {
            userTaskAdapter.setData(it)
        }
    }
}
