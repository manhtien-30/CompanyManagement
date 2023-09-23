package com.example.companymanagement.viewcontroller.fragment.mainproject

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.utils.DateTimeExt.Companion.toLocalDate

import com.example.companymanagement.utils.customize.DotDateView.DateEvent
import com.example.companymanagement.utils.customize.DotDateView.EventCalendarView
import com.example.companymanagement.viewcontroller.adapter.UserTaskAdapter
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.*

class MainProject : Fragment() {

    private var viewModelMainProject = MainProjectViewModel()

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_main_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.task_recyclerView)
        val userTaskAdapter = UserTaskAdapter()
        val taskLayoutManager = LinearLayoutManager(context)
        val calendarView = view.findViewById<EventCalendarView>(R.id.main_project_calendar)
        //
        val cal = Calendar.getInstance()
        cal.set(Calendar.DATE, 1)
        var start = cal.toInstant();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        var end = cal.toInstant();
        taskLayoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.adapter = userTaskAdapter
        recyclerView.layoutManager = taskLayoutManager
        //Load data at current date
        viewModelMainProject.retrieveUserTask(
            user?.uid!!, Date.from(start), Date.from(end))

        calendarView.setOnMonthChangeListener { _, new, _ ->
            calendarView.clearEvent()
            cal.set(new.year, new.monthValue - 1, 1)
            var start = cal.toInstant();
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
            var end = cal.toInstant();
            Log.d("month", new.toString())
            viewModelMainProject.retrieveUserTask(
                user?.uid!!, Date.from(start), Date.from(end))

        }

        viewModelMainProject.taskList.observe(viewLifecycleOwner) {
            var mapdoneandundone = it.groupBy { it.Status == "done" }
            var donedatevent = mapdoneandundone.get(true)?.map {
                DateEvent(
                    it.Deadline!!.toLocalDate(), Color.valueOf(Color.GREEN))
            }
            var undonedatevent = mapdoneandundone.get(false)?.map {
                DateEvent(
                    it.Deadline!!.toLocalDate(), Color.valueOf(Color.RED))
            }
            if (donedatevent != null)
                calendarView.addAllEvent(donedatevent)
            if (undonedatevent != null)
                calendarView.addAllEvent(undonedatevent)
        }
        userTaskAdapter.setOnClickDone { data, v ->
            viewModelMainProject.updateStatus(data, "done")
            calendarView.addEvent(DateEvent(data.Deadline!!.toLocalDate(),
                Color.valueOf(Color.GREEN)))
            v.hideButton()

        }
        userTaskAdapter.setOnClickUndone() { data, v ->
            viewModelMainProject.updateStatus(data, "undone")
            calendarView.addEvent(DateEvent(data.Deadline!!.toLocalDate(),
                Color.valueOf(Color.RED)))
            v.hideButton()
        }

        calendarView.setDaySelectChangeListener { v, d ->

            //clear old data before loading a new one
            userTaskAdapter.clear()
            var daylist = viewModelMainProject.taskList.value?.groupBy {
                it.Deadline!!.toLocalDate() == d
            }
            if (daylist?.get(true) != null) {
                userTaskAdapter.setData(daylist?.get(true)!!.toMutableList())
            }


        }

    }
}